package org.example.service;

import lombok.Getter;
import org.example.DTO.AbstractFinProduct;
import org.example.DTO.ProductRequest;
import org.example.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Getter
public class ApplicationProcessingService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationProcessingService.class);

    private static final ApplicationProcessingService INSTANCE = new ApplicationProcessingService();

    private final Map<UUID, AbstractFinProduct> products = new HashMap<>();

    private final List<ProductRequest> savedRequests = new ArrayList<>();
    private final RequestValidationService validationService = new RequestValidationService();

    private ApplicationProcessingService() {
    }

    public static ApplicationProcessingService getInstance() {
        return INSTANCE;
    }

    public void registerProduct(AbstractFinProduct product) {
        products.put(product.getId(), product);
    }

    public void processRequest(ProductRequest request) throws BusinessException {
        AbstractFinProduct product = findProductById(request.getProductId());

        validationService.validate(request, product);

        savedRequests.add(request);
        log.info("Заявка сохранена: requestId={}, productId={}, amount={}",
                request.getId(), request.getProductId(), request.getAmount());
    }

    private AbstractFinProduct findProductById(UUID productId) {
        AbstractFinProduct product = products.get(productId);
        if (product == null) {
            log.error("Продукт не найден: productId={}", productId);
            throw new IllegalArgumentException("Продукт с id=" + productId + " не найден");
        }
        return product;
    }

}