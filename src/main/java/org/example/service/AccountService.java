package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Account;
import org.example.exception.AccountLockException;
import org.example.exception.BusinessException;
import org.example.exception.CurrencyMismatchException;
import org.example.exception.InsufficientFundsException;
import org.example.repository.Repository;

import org.example.exception.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private static final long LOCK_TIMEOUT_MS = 500;
    private static final int MONEY_SCALE = 2;

    private final Repository <Account, UUID> accountRepository;
    private final Map<UUID, ReentrantLock> locks = new ConcurrentHashMap<>();

    public void deposit(UUID accountId, BigDecimal amount) throws BusinessException {
        checkAmount(amount);
        Account account = findAccount(accountId);
        ReentrantLock lock =lockFor(accountId);

        acquire(lock, accountId);
        try{
            credit(account, amount);
        } finally {
            lock.unlock();
        }
        log.info("Deposit: accountId={}, amount={}", accountId, amount);
    }

    public void withdraw(UUID accountId, BigDecimal amount) throws BusinessException {
        checkAmount(amount);
        Account account = findAccount(accountId);
        ReentrantLock lock = lockFor(accountId);

        acquire(lock, accountId);
        try{
            debit(account, amount);
        }finally {
            lock.unlock();
        }
        log.info("Withdraw: accountId={}, amount={}", accountId, amount);
    }

    public void transfer(UUID fromId, UUID toId, BigDecimal amount) throws BusinessException {
        checkAmount(amount);
        Objects.requireNonNull(fromId, "fromId cannot be null");
        Objects.requireNonNull(toId, "toId cannot be null");
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        Account from = findAccount(fromId);
        Account to = findAccount(toId);
        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new CurrencyMismatchException(fromId, from.getCurrency(), toId, to.getCurrency());
        }
        UUID firstId = fromId.compareTo(toId) < 0 ? fromId : toId;
        UUID secondId = firstId.equals(fromId) ? toId : fromId;
        ReentrantLock first = lockFor(firstId);
        ReentrantLock second = lockFor(secondId);

        acquire(first, firstId);
        try {
            acquire(second, secondId);
        try {
            debit(from, amount);
            credit(to, amount);
        } finally {
            second.unlock();
        }
        } finally {
            first.unlock();
        }
        log.info("Transfer: fromId={}, toId={}, amount={}", fromId, toId, amount);
    }
    public BigDecimal getBalance(UUID accountId) throws AccountNotFoundException {
        return findAccount(accountId).getBalance();
    }
    private void credit(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().add(amount));
    }
    private void debit(Account account, BigDecimal amount) throws InsufficientFundsException {
        BigDecimal balance = account.getBalance();
        if (balance.compareTo(amount) < 0) {
            log.warn("Insufficient funds: accountId={}, balance={}, amount={}", account.getId(), balance, amount);
            throw new InsufficientFundsException(account.getId(), balance, amount);
        }
        account.setBalance(balance.subtract(amount));
    }
    private ReentrantLock lockFor(UUID accountId) {
        return locks.computeIfAbsent(accountId, id -> new ReentrantLock());
    }

    private void acquire(ReentrantLock lock, UUID accountId) throws AccountLockException {
        try {
            if (!lock.tryLock(LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                log.error("Lock timeout: accountId={}", accountId);
                throw new AccountLockException(accountId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AccountLockException(accountId, e);
        }
    }
    private Account findAccount(UUID accountId) throws AccountNotFoundException {
        Objects.requireNonNull(accountId, "accountId cannot be null");
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private void checkAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount.stripTrailingZeros().scale() > MONEY_SCALE) {
            throw new IllegalArgumentException("Amount cannot have more than " + MONEY_SCALE + " decimal places");
        }
    }
}
