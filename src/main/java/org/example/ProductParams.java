package org.example;

import lombok.Builder;
import lombok.Getter;


import java.util.UUID;
@Getter
@Builder
public class ProductParams {
    private UUID id;
    private String nameProduct;
    private String description;
    private boolean active;
    private double minAmount;
    private double maxAmount;
    private String currency;
    private CreditType creditType;
    private boolean hasCollateral;
    private String paymentSchedule;
    private boolean replenishmenAllowed;
    private String closureCondition;
}
