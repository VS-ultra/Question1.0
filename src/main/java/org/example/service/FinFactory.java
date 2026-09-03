package org.example.service;

import org.example.DTO.AbstractFinProduct;
import org.example.DTO.ProductParams;
import org.example.ENUM.CreditType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinFactory {

    private static final Logger log = LoggerFactory.getLogger(FinFactory.class);

    public static AbstractFinProduct createFinProduct(String type, ProductParams params) {
        if (type == null) {
            throw new IllegalArgumentException("Тип продукта не может быть null");
        }

        AbstractFinProduct product;

        switch (type.toUpperCase()) {
            case "CREDIT":
                product = new CreditProduct(
                        params.getId(),
                        params.getNameProduct(),
                        params.getDescription(),
                        params.isActive(),
                        params.getMinAmount(),
                        params.getMaxAmount(),
                        params.getCurrency(),
                        params.getCreditType(),
                        params.isHasCollateral(),
                        params.getPaymentSchedule()
                );
                break;
            case "DEPOSIT":
                product = new DepositProduct(
                        params.getId(),
                        params.getNameProduct(),
                        params.getDescription(),
                        params.isActive(),
                        params.getMinAmount(),
                        params.getMaxAmount(),
                        params.getCurrency(),
                        params.isReplenishmentAllowed(),
                        params.getClosureCondition()
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }

        log.info("Created product successfully: id={}, type={}, nameProduct={}",
                product.getId(), type, product.getNameProduct());

        return product;
    }
}