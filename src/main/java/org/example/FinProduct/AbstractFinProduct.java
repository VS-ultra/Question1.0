package org.example.FinProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractFinProduct implements InterfaceFinProduct {
    private UUID id;
    private String nameProduct;
    private String description;
    private boolean active;
    private double minAmount;
    private double maxAmount;
    private String currency;
}
