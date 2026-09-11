package org.example;

import org.example.dto.*;
import org.example.ENUM.CreditType;
import org.example.ENUM.ProductType;
import org.example.ENUM.RequestStatus;
import org.example.exception.BusinessException;
import org.example.repository.InMemoryRepository;
import org.example.repository.Repository;
import org.example.service.ApplicationProcessingService;
import org.example.service.ProductAnalyticsService;
import org.example.service.RequestAnalyticsService;

import java.time.LocalDateTime;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws BusinessException {

        // --- Репозитории ---
        Repository<AbstractFinProduct, UUID> productRepository = new InMemoryRepository<>();
        Repository<ProductRequest, UUID> requestRepository = new InMemoryRepository<>();
        Repository<Client, UUID> clientRepository = new InMemoryRepository<>();

        // --- Продукты ---
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

        // --- Клиенты ---
        Client youngClient = new Client(UUID.randomUUID(), "Анна", 27);
        Client olderClient = new Client(UUID.randomUUID(), "Сергей", 42);
        clientRepository.save(youngClient);
        clientRepository.save(olderClient);

        // --- Заявки ---
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

        DepositProduct unclaimedDeposit = new DepositProduct(
                UUID.randomUUID(), "Вклад Онлайн", "Вклад без заявок за период", true,
                5000, 1000000, "RUB", 0.05,
                false, "Без досрочного закрытия"
        );
        productRepository.save(unclaimedDeposit);

        // --- Аналитика по продуктам ---
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

        // --- Аналитика по заявкам и клиентам ---
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

        // --- Проверка ApplicationProcessingService (Singleton) ---
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
}