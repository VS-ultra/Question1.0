package org.example.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends BusinessException {
    public InsufficientFundsException(UUID accountId, BigDecimal balance, BigDecimal amount) {
        super(String.format("Insufficient funds on account %s: balance=%s, requested=%s", accountId, balance, amount));
    }
}