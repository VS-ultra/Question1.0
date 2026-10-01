package org.example.exception;

import java.util.UUID;

public class CurrencyMismatchException extends BusinessException {
    public CurrencyMismatchException(UUID fromId, String fromCurrency, UUID toId, String toCurrency) {
        super(String.format("Currency mismatch: account %s (%s) -> account %s (%s)",
                fromId, fromCurrency, toId, toCurrency));
    }
}