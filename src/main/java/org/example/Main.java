package org.example;

import org.example.FabFinProduct.ProductRequest;
import org.example.FinProduct.CreditProduct;
import org.example.FinProduct.CreditType;
import org.example.ValidationRequest.BusinessException;
import org.example.ValidationRequest.RequestValidationService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        CreditProduct credit = new CreditProduct(
                UUID.randomUUID(), "Ипотека", "Ипотечный кредит", false, // <- продукт неактивен
                500000, 10000000, "RUB",
                CreditType.MORTGAGE, true, "ANNUITY"
        );

        ProductRequest request = ProductRequest.builder()
                .id(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .productId(credit.getId())
                .amount(1000000)
                .build();

        RequestValidationService validationService = new RequestValidationService();

        try {
            validationService.validate(request, credit);
            System.out.println("Заявка прошла валидацию");
        } catch (BusinessException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }
}