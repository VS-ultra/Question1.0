package org.example;

import lombok.Getter;

import java.util.UUID;
@Getter
public class CreditProduct extends AbstractFinProduct {

    private final CreditType creditType;
    private final boolean hasCollateral;
    private final String paymentSchedule;

    public CreditProduct(UUID id, String nameProduct, String description, boolean active, double minAmount, double maxAmount, String currency, CreditType creditType, boolean hasCollateral, String paymentSchedule) {
        super(id, nameProduct, description, active, minAmount, maxAmount, currency);
        this.creditType = creditType;
        this.hasCollateral = hasCollateral;
        this.paymentSchedule = paymentSchedule;
    }
}
