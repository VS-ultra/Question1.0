package org.example.dto;

import lombok.Getter;
import org.example.ENUM.ProductType;
import org.example.ENUM.CreditType;

import java.util.UUID;
@Getter
public class CreditProduct extends AbstractFinProduct {

    private final CreditType creditType;
    private final boolean hasCollateral;
    private final String paymentSchedule;

    public CreditProduct(UUID id, String nameProduct, String description, boolean active, double minAmount, double maxAmount, String currency, double interestRate, CreditType creditType, boolean hasCollateral, String paymentSchedule) {
        super(id, nameProduct, description, active, minAmount, maxAmount, currency, interestRate, ProductType.CREDIT);
        this.creditType = creditType;
        this.hasCollateral = hasCollateral;
        this.paymentSchedule = paymentSchedule;
    }
}
