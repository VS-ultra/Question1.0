package org.example.service;

import org.example.DTO.ProductRequest;
import org.example.DTO.AbstractFinProduct;
import org.example.exception.BusinessException;
import org.example.exception.InvalidAmountException;
import org.example.exception.ProductNotActiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestValidationService {
    private static final Logger log = LoggerFactory.getLogger(RequestValidationService.class);
    public void validate(ProductRequest request, AbstractFinProduct product) throws BusinessException {
        try {
            checkProductActive(product);
            checkAmountInRange(request, product);
            log.info("Request validated successfully: id={}, amount={}", request.getId(), request.getAmount());
        }catch (BusinessException e){
            log.error("Request validation failed: id={}, amount={}", request.getId(), request.getAmount());
            throw e;
        }

    }
    private void checkProductActive(AbstractFinProduct product) throws ProductNotActiveException {
        if (!product.isActive()) {
            throw new ProductNotActiveException(product.getId());
        }
    }
    private void checkAmountInRange(ProductRequest request, AbstractFinProduct product) throws InvalidAmountException {
        double amount = request.getAmount();
        double min = product.getMinAmount();
        double max = product.getMaxAmount();
        if (amount < min || amount > max) {
            throw new InvalidAmountException(product.getId(), amount, min, max);
        }
    }
}
