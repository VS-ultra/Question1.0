package org.example;

import org.example.DTO.ProductRequest;
import org.example.ENUM.CreditType;
import org.example.exception.BusinessException;
import org.example.service.ApplicationProcessingService;
import org.example.service.CreditProduct;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        CreditProduct credit = new CreditProduct(
                UUID.randomUUID(), "Ипотека", "Ипотечный кредит", true,
                500000, 10000000, "RUB",
                CreditType.MORTGAGE, true, "ANNUITY"
        );

        ApplicationProcessingService processor = ApplicationProcessingService.getInstance();

        processor.registerProduct(credit);

        ProductRequest request = ProductRequest.builder()
                .id(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .productId(credit.getId())
                .amount(1000000)
                .build();

        try {
            processor.processRequest(request);
            System.out.println("Заявка обработана. Всего сохранённых заявок: " + processor.getSavedRequests().size());
        } catch (BusinessException e) {
            System.out.println("Ошибка обработки заявки: " + e.getMessage());
        }
    }
}