package org.example.FabFinProduct;

import org.example.FinProduct.AbstractFinProduct;
import org.example.FinProduct.CreditProduct;
import org.example.FinProduct.DepositProduct;

public class FinFactory {
    public static AbstractFinProduct createFinProduct(String type, ProductParams params){
        if (type == null){
            throw new IllegalArgumentException("Type is null");
        }

        switch (type.toUpperCase()){
            case "CREDIT":
                return new CreditProduct(
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
            case "DEPOSIT":
                return new DepositProduct(
                        params.getId(),
                        params.getNameProduct(),
                        params.getDescription(),
                        params.isActive(),
                        params.getMinAmount(),
                        params.getMaxAmount(),
                        params.getCurrency(),
                        params.isReplenishmenAllowed(),
                        params.getClosureCondition()
                );
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}