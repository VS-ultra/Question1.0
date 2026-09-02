package org.example.FabFinProduct;

import lombok.Builder;
import lombok.Getter;
import org.example.FinProduct.CreditType;


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
    // Thats only credit
    private CreditType creditType;
    private boolean hasCollateral;
    private String paymentSchedule;
    // Thats only deposit
    private boolean replenishmenAllowed;
    private String closureCondition;
}
