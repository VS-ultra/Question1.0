package org.example.dto;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinFactory {

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
                        params.getInterestRate(),
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
                        params.getInterestRate(),
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