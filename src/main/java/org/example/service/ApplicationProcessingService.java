package org.example.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.ENUM.RequestStatus;
import org.example.concurrency.OperationCounter;
import org.example.concurrency.OperationStatistics;
import org.example.dto.AbstractFinProduct;
import org.example.dto.ProductRequest;
import org.example.exception.BusinessException;
import org.example.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
public class ApplicationProcessingService {

    private static final ApplicationProcessingService INSTANCE = new ApplicationProcessingService();

    private final Map<UUID, AbstractFinProduct> products = new ConcurrentHashMap<>();
    private final Map<UUID,ProductRequest> savedRequests = new ConcurrentHashMap<>();
    private final RequestValidationService validationService = new RequestValidationService();
    private final OperationCounter operationCounter = new OperationCounter();
    private final OperationStatistics operationStatistics = new OperationStatistics();

    private ApplicationProcessingService() {
    }

    public static ApplicationProcessingService getInstance() {
        return INSTANCE;
    }

    public void registerProduct(AbstractFinProduct product) {
        products.put(product.getId(), product);
    }

    public ProductRequest processRequest(ProductRequest request) throws BusinessException {
        AbstractFinProduct product = findProductById(request.getProductId());
        validationService.validate(request, product);

        ProductRequest approvedRequest = request.withStatus(RequestStatus.APPROVED);
        ProductRequest previous = savedRequests.putIfAbsent(approvedRequest.getId(), approvedRequest);
        if (previous != null){
            log.warn("The request has been processed, requestId = {}, productID={}, amount={}, status={}", request.getId(), request.getProductId(), request.getAmount(), request.getStatus());
            return previous;
        }
        operationCounter.increment(product.getProductType());
        operationStatistics.record(product.getProductType(), BigDecimal.valueOf(approvedRequest.getAmount()));
        log.info("Заявка сохранена: requestId={}, productId={}, amount={}, status={}", approvedRequest.getId(), approvedRequest.getProductId(), approvedRequest.getAmount(), approvedRequest.getStatus());
        return approvedRequest;
    }

    private AbstractFinProduct findProductById(UUID productId) throws NotFoundException {
        AbstractFinProduct product = products.get(productId);
        if (product == null) {
            log.error("Продукт не найден: productId={}", productId);
            throw new NotFoundException(productId);
        }
        return product;
    }

}