package org.example;

import org.example.ENUM.CreditType;
import org.example.ENUM.ProductType;
import org.example.ENUM.RequestStatus;
import org.example.concurrency.OperationCounter;
import org.example.concurrency.OperationStatistics;
import org.example.concurrency.PriorityTaskExecutor;
import org.example.concurrency.RequestDispatcher;
import org.example.concurrency.Stats;
import org.example.concurrency.config.ThreadPoolConfig;
import org.example.dto.*;
import org.example.exception.BusinessException;
import org.example.exception.InsufficientFundsException;
import org.example.repository.InMemoryRepository;
import org.example.repository.Repository;
import org.example.service.*;
import org.example.source.FakeExchangeRateSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws Exception {
        // Этапы 1 и 2
        demoStages1And2();

        // Этап 3
        demoDoubleSpend();
        demoTransfers();
        demoRequestQueue();
        demoPriorityPool();
        demoExchangeRates();
    }

    // Этапы 1 и 2. Объектная модель, аналитика, Singleton
    private static void demoStages1And2() throws BusinessException {
        printHeader("Этапы 1 и 2. Объектная модель, аналитика, Singleton");

        // Этап 2. Репозитории
        Repository<AbstractFinProduct, UUID> productRepository = new InMemoryRepository<>();
        Repository<ProductRequest, UUID> requestRepository = new InMemoryRepository<>();
        Repository<Client, UUID> clientRepository = new InMemoryRepository<>();

        // Этап 1. Иерархия продуктов
        CreditProduct credit = new CreditProduct(
                UUID.randomUUID(), "Ипотека", "Ипотечный кредит", true,
                500000, 10000000, "RUB", 0.08,
                CreditType.MORTGAGE, true, "ANNUITY"
        );
        DepositProduct deposit = new DepositProduct(
                UUID.randomUUID(), "Вклад Классический", "Вклад с капитализацией", true,
                10000, 5000000, "RUB", 0.045,
                true, "Потеря процентов при досрочном закрытии"
        );
        productRepository.save(credit);
        productRepository.save(deposit);

        // Этап 2. Клиенты для аналитики
        Client youngClient = new Client(UUID.randomUUID(), "Анна", 27);
        Client olderClient = new Client(UUID.randomUUID(), "Сергей", 42);
        clientRepository.save(youngClient);
        clientRepository.save(olderClient);

        // Этап 1. Заявки через builder
        ProductRequest request1 = ProductRequest.builder()
                .id(UUID.randomUUID())
                .clientId(youngClient.getId())
                .productId(credit.getId())
                .amount(1500000)
                .termMonths(120)
                .status(RequestStatus.APPROVED)
                .createdAt(LocalDateTime.now().minusDays(5))
                .build();

        ProductRequest request2 = ProductRequest.builder()
                .id(UUID.randomUUID())
                .clientId(olderClient.getId())
                .productId(credit.getId())
                .amount(2000000)
                .termMonths(180)
                .status(RequestStatus.REJECTED)
                .createdAt(LocalDateTime.now().minusDays(10))
                .build();

        ProductRequest request3 = ProductRequest.builder()
                .id(UUID.randomUUID())
                .clientId(youngClient.getId())
                .productId(deposit.getId())
                .amount(300000)
                .status(RequestStatus.APPROVED)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        requestRepository.save(request1);
        requestRepository.save(request2);
        requestRepository.save(request3);

        // Этап 2. Данные и аналитика
        DepositProduct unclaimedDeposit = new DepositProduct(
                UUID.randomUUID(), "Вклад Онлайн", "Вклад без заявок за период", true,
                5000, 1000000, "RUB", 0.05,
                false, "Без досрочного закрытия"
        );
        productRepository.save(unclaimedDeposit);

        ProductAnalyticsService productAnalytics =
                new ProductAnalyticsService(productRepository, requestRepository);

        System.out.println("=== Средняя ставка по типам ===");
        System.out.println(productAnalytics.averageRateByType());

        System.out.println("\n=== Средняя мин. сумма по типам ===");
        System.out.println(productAnalytics.averageMinAmountByType());

        System.out.println("\n=== Активные продукты: ставка > 0.05, мин. сумма < 600000 ===");
        System.out.println(productAnalytics.filterActiveProducts(0.05, 600000));

        System.out.println("\n=== Невостребованные продукты за последние 30 дней ===");
        System.out.println(productAnalytics.findUnclaimedProducts(
                LocalDateTime.now().minusDays(30), LocalDateTime.now()));

        System.out.println("\n=== Ранжирование по популярности ===");
        productAnalytics.rankByPopularity().forEach(pp ->
                System.out.println(pp.getProduct().getNameProduct() + " | заявок: " + pp.getRequestCount()
                        + " | одобрено: " + pp.getApprovalRate() + "%"));

        System.out.println("\n=== Заявки по продуктам типа CREDIT ===");
        System.out.println(productAnalytics.findRequestsByProductType(ProductType.CREDIT));

        RequestAnalyticsService requestAnalytics =
                new RequestAnalyticsService(requestRepository, clientRepository);

        System.out.println("\n=== Средняя сумма заявки ===");
        System.out.println(requestAnalytics.averageRequestAmount());

        System.out.println("\n=== Максимальный срок ===");
        System.out.println(requestAnalytics.maxTerm());

        System.out.println("\n=== Распределение по статусам (%) ===");
        System.out.println(requestAnalytics.statusDistributionPercent());

        System.out.println("\n=== Средняя сумма по возрастным категориям ===");
        System.out.println(requestAnalytics.averageAmountByAgeCategory());

        // Этап 1. Обработка заявки через Singleton
        ApplicationProcessingService processor = ApplicationProcessingService.getInstance();
        processor.registerProduct(credit);

        ProductRequest newRequest = ProductRequest.builder()
                .id(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .productId(credit.getId())
                .amount(1000000)
                .build();

        try {
            processor.processRequest(newRequest);
            System.out.println("\nЗаявка обработана через Singleton. Сохранённых заявок: "
                    + processor.getSavedRequests().size());
        } catch (BusinessException e) {
            System.out.println("Ошибка обработки заявки: " + e.getMessage());
        }
    }

    // Этап 3.1 Безопасное списание: защита от двойного списания
    private static void demoDoubleSpend() throws InterruptedException {
        printHeader("Этап 3.1 Защита от двойного списания");

        Repository<Account, UUID> accountRepository = new InMemoryRepository<>();
        AccountService accountService = new AccountService(accountRepository);

        Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), "RUB", new BigDecimal("100.00"));
        accountRepository.save(account);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();

        runConcurrently(50, threadIndex -> {
            try {
                accountService.withdraw(account.getId(), new BigDecimal("10.00"));
                success.incrementAndGet();
            } catch (InsufficientFundsException e) {
                rejected.incrementAndGet();
            }
        });

        System.out.println("50 потоков списывают по 10 со счёта, на котором 100");
        System.out.println("Успешных списаний: " + success.get() + " (ожидается 10)");
        System.out.println("Отказов:           " + rejected.get() + " (ожидается 40)");
        System.out.println("Итоговый баланс:   " + account.getBalance() + " (ожидается 0.00)");
    }

    // Этап 3.2 Безопасные переводы между счетами
    private static void demoTransfers() throws Exception {
        printHeader("Этап 3.2 Параллельные переводы между счетами");

        Repository<Account, UUID> accountRepository = new InMemoryRepository<>();
        AccountService accountService = new AccountService(accountRepository);

        List<UUID> accountIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Account account = new Account(UUID.randomUUID(), UUID.randomUUID(), "RUB", new BigDecimal("1000.00"));
            accountRepository.save(account);
            accountIds.add(account.getId());
        }

        AtomicInteger transfers = new AtomicInteger();
        AtomicInteger rejectedTransfers = new AtomicInteger();

        runConcurrently(8, threadIndex -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < 1000; i++) {
                UUID from = accountIds.get(random.nextInt(accountIds.size()));
                UUID to = accountIds.get(random.nextInt(accountIds.size()));
                if (from.equals(to)) {
                    continue;
                }
                try {
                    accountService.transfer(from, to, BigDecimal.valueOf(random.nextInt(1, 200)));
                    transfers.incrementAndGet();
                } catch (InsufficientFundsException e) {
                    rejectedTransfers.incrementAndGet();
                }
            }
        });

        BigDecimal total = BigDecimal.ZERO;
        for (UUID id : accountIds) {
            BigDecimal balance = accountService.getBalance(id);
            System.out.println("Счёт " + id.toString().substring(0, 8) + ": " + balance);
            total = total.add(balance);
        }
        System.out.println("Выполнено переводов: " + transfers.get());
        System.out.println("Отклонено из-за нехватки средств: " + rejectedTransfers.get());
        System.out.println("Сумма по всем счетам: " + total + " (ожидается 5000.00)");
    }

    // Этап 3.3 Очередь заявок, распределение нагрузки, счётчик и статистика
    private static void demoRequestQueue() throws Exception {
        printHeader("Этап 3.3 Очередь заявок и распределение нагрузки");

        ApplicationProcessingService processor = ApplicationProcessingService.getInstance();

        CreditProduct credit = new CreditProduct(
                UUID.randomUUID(), "Ипотека", "Ипотечный кредит", true,
                500000, 10000000, "RUB", 0.08,
                CreditType.MORTGAGE, true, "ANNUITY");
        DepositProduct deposit = new DepositProduct(
                UUID.randomUUID(), "Вклад Классический", "Вклад с капитализацией", true,
                10000, 5000000, "RUB", 0.045,
                true, "Потеря процентов при досрочном закрытии");
        processor.registerProduct(credit);
        processor.registerProduct(deposit);

        Map<ProductType, Long> counterBefore = processor.getOperationCounter().snapshot();

        RequestDispatcher dispatcher = new RequestDispatcher(4, 50, processor);
        dispatcher.start();

        runConcurrently(4, threadIndex -> {
            for (int i = 1; i <= 50; i++) {
                boolean isCredit = i % 2 == 0;
                double amount = isCredit ? 1_000_000 : 100_000;
                if (i % 25 == 0) {
                    amount = 1;
                }
                dispatcher.submit(ProductRequest.builder()
                        .id(UUID.randomUUID())
                        .clientId(UUID.randomUUID())
                        .productId(isCredit ? credit.getId() : deposit.getId())
                        .amount(amount)
                        .build());
            }
        });

        dispatcher.shutdown();

        System.out.println("Отправлено заявок: 200 (из них 8 с неверной суммой)");
        System.out.println("Заявок на каждого обработчика: " + dispatcher.getProcessedByWorker());

        OperationCounter counter = processor.getOperationCounter();
        OperationStatistics statistics = processor.getOperationStatistics();
        System.out.println("\nОдобрено в этой части:");
        for (ProductType type : ProductType.values()) {
            long added = counter.get(type) - counterBefore.get(type);
            System.out.println(type + "=" + added);
        }
        System.out.println("Ожидается: CREDIT=96, DEPOSIT=96");

        System.out.println("\nСтатистика по суммам (всего с начала работы программы):");
        for (ProductType type : ProductType.values()) {
            Stats stats = statistics.get(type);
            System.out.println(type + ": количество=" + stats.count()
                    + ", сумма=" + stats.sum()
                    + ", мин=" + stats.min()
                    + ", макс=" + stats.max()
                    + ", среднее=" + stats.average());
        }
    }

    // Этап 3.4 Пул потоков с приоритетами
    private static void demoPriorityPool() {
        printHeader("Этап 3.4 Пул потоков с приоритетами");

        CountDownLatch gate = new CountDownLatch(1);
        List<String> executionOrder = Collections.synchronizedList(new ArrayList<>());

        try (PriorityTaskExecutor executor = new PriorityTaskExecutor(new ThreadPoolConfig(1, "bank-worker", 10))) {
            executor.submitClientTask("blocker", () -> {
                try {
                    gate.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return null;
            });

            executor.submitAnalyticsTask("Отчёт-1", () -> executionOrder.add("Отчёт-1 (LOW)"));
            executor.submitClientTask("Перевод-1", () -> executionOrder.add("Перевод-1 (HIGH)"));
            executor.submitAnalyticsTask("Отчёт-2", () -> executionOrder.add("Отчёт-2 (LOW)"));
            executor.submitClientTask("Перевод-2", () -> executionOrder.add("Перевод-2 (HIGH)"));
            executor.submitAnalyticsTask("Отчёт-3", () -> executionOrder.add("Отчёт-3 (LOW)"));
            executor.submitClientTask("Перевод-3", () -> executionOrder.add("Перевод-3 (HIGH)"));

            gate.countDown();
        }

        System.out.println("Порядок отправки:   Отчёт-1, Перевод-1, Отчёт-2, Перевод-2, Отчёт-3, Перевод-3");
        System.out.println("Порядок выполнения: " + String.join(", ", executionOrder));
    }

    // Этап 3.5 Курсы валют: параллельная загрузка, таймаут, fallback
    private static void demoExchangeRates() {
        printHeader("Этап 3.5 Курсы валют");

        FakeExchangeRateSource cbr = new FakeExchangeRateSource("ЦБ", new BigDecimal("90.00"), 300);
        FakeExchangeRateSource exchange = new FakeExchangeRateSource("Биржа", new BigDecimal("91.00"), 300);
        FakeExchangeRateSource partner = new FakeExchangeRateSource("Партнёр", new BigDecimal("95.00"), 2000);

        ExchangeRateService rateService = new ExchangeRateService(List.of(cbr, exchange, partner), 1000);

        long start = System.currentTimeMillis();
        BigDecimal usd = rateService.getRate("USD").join();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("ЦБ и Биржа отвечают за 300 мс, Партнёр за 2000 мс, таймаут 1000 мс");
        System.out.println("Курс USD: " + usd + " за " + elapsed + " мс");
        System.out.println("Партнёр отсечён по таймауту, курс — среднее ЦБ и Биржи");

        cbr.setAvailable(false);
        exchange.setAvailable(false);
        usd = rateService.getRate("USD").join();
        System.out.println("\nВсе источники недоступны");
        System.out.println("Курс USD: " + usd + " — последний известный курс");

        rateService.shutdown();
    }

    // Этап 3. Вспомогательные методы
    @FunctionalInterface
    private interface ConcurrentTask {
        void run(int threadIndex) throws Exception;
    }

    private static void runConcurrently(int threadCount, ConcurrentTask task) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int threadIndex = i;
            pool.execute(() -> {
                try {
                    start.await();
                    task.run(threadIndex);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.out.println("Ошибка в потоке " + threadIndex + ": " + e.getMessage());
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        done.await();
        pool.shutdown();
    }

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println(title);
        System.out.println("=".repeat(60));
    }
}