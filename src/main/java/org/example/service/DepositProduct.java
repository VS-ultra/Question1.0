package org.example.service;

import lombok.Getter;
import org.example.DTO.AbstractFinProduct;

import java.util.UUID;

@Getter
public class DepositProduct extends AbstractFinProduct {
    private final boolean replenishmenAllowed;
    private final String closureCondition;


    public DepositProduct(UUID id, String nameProduct, String description, boolean active, double minAmount, double maxAmount, String currency, boolean replenishmenAllowed, String closureCondition) {
        super(id, nameProduct, description, active, minAmount, maxAmount, currency);
        this.replenishmenAllowed = replenishmenAllowed;
        this.closureCondition = closureCondition;

    }
}
