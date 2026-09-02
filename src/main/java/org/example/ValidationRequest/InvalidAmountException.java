package org.example.ValidationRequest;

import java.util.UUID;

public class InvalidAmountException extends BusinessException {
    public InvalidAmountException(UUID productId, double amount, double minAmount, double maxAmount) {
        super(String.format("Invalid amount for product %s: %s is not between %s and %s", productId, amount, minAmount, maxAmount));
    }
}
