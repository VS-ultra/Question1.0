package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.ENUM.ProductType;
import org.example.repository.Identifiable;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractFinProduct implements InterfaceFinProduct, Identifiable<UUID> {
    private UUID id;
    private String nameProduct;
    private String description;
    private boolean active;
    private double minAmount;
    private double maxAmount;
    private String currency;
    private double interestRate;
    private ProductType productType;
}
