package org.example.service;

import org.example.ENUM.ProductType;
import org.example.ENUM.RequestStatus;
import org.example.dto.AbstractFinProduct;
import org.example.dto.ProductPopularity;
import org.example.dto.ProductRequest;
import org.example.repository.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ProductAnalyticsService {
    private final Repository<AbstractFinProduct, UUID> productRepository;
    private final Repository<ProductRequest, UUID> requestRepository;

    public ProductAnalyticsService(Repository<AbstractFinProduct, UUID> productRepository, Repository<ProductRequest, UUID> requestRepository) {
        this.productRepository = productRepository;
        this.requestRepository = requestRepository;
    }
    public Map<ProductType, Double> averageRateByType() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        AbstractFinProduct::getProductType,
                        Collectors.averagingDouble(AbstractFinProduct::getInterestRate)));

    }
    public Map<ProductType, Double> averageMinAmountByType() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        AbstractFinProduct::getProductType,
                        Collectors.averagingDouble(AbstractFinProduct::getMinAmount)));
    }
    public List<AbstractFinProduct> filterActiveProducts(double minRateExclusive, double maxMinAmountExclusive) {
        return productRepository.findAll().stream()
                .filter(AbstractFinProduct::isActive)
                .filter(p -> p.getInterestRate() > minRateExclusive)
                .filter(p -> p.getMinAmount() < maxMinAmountExclusive)
                .collect(Collectors.toList());
    }
    public List<AbstractFinProduct> findUnclaimedProducts(LocalDateTime periodStart, LocalDateTime periodEnd) {
        Set<UUID> claimedProductIds = requestRepository.findAll().stream()
                .filter(r -> !r.getCreatedAt().isBefore(periodStart)&&!r.getCreatedAt().isAfter(periodEnd))
                .map(ProductRequest::getProductId)
                .collect(Collectors.toSet());
        return productRepository.findAll().stream()
                .filter(p -> !claimedProductIds.contains(p.getId()))
                .collect(Collectors.toList());
    }
    public List<ProductPopularity> rankByPopularity() {
        Map<UUID, List<ProductRequest>> requestsByProduct = requestRepository.findAll().stream()
                .collect(Collectors.groupingBy(ProductRequest::getProductId));

        return productRepository.findAll().stream()
                .map(product -> {
                    List<ProductRequest> requests = requestsByProduct.getOrDefault(product.getId(), List.of());
                    long total = requests.size();
                    long approved = requests.stream()
                            .filter(r -> r.getStatus() == RequestStatus.APPROVED)
                            .count();
                    double approvalRate = total == 0 ? 0.0 : (approved * 100.0) / total;
                    return new ProductPopularity(product, total, approvalRate);
                })
                .sorted(Comparator.comparingLong(ProductPopularity::getRequestCount).reversed()
                        .thenComparing(Comparator.comparingDouble(ProductPopularity::getApprovalRate).reversed()))
                .collect(Collectors.toList());
    }
    public List<ProductRequest> findRequestsByProductType(ProductType type){
        Set<UUID> productIds = productRepository.findAll().stream()
                .filter(p -> p.getProductType() == type)
                .map(AbstractFinProduct::getId)
                .collect(Collectors.toSet());
        return requestRepository.findAll().stream()
                .filter(r -> productIds.contains(r.getProductId()))
                .collect(Collectors.toList());
    }

}
