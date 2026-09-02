package org.example.ValidationRequest;

import org.example.FabFinProduct.ProductRequest;
import org.example.FinProduct.AbstractFinProduct;

public class RequestValidationService {
    public void validate(ProductRequest request, AbstractFinProduct product) throws BusinessException {
        checkProductActive(product);
        checkAmountInRange(request, product);
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
