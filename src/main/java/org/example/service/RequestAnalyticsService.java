package org.example.service;

import org.example.ENUM.RequestStatus;
import org.example.dto.Client;
import org.example.dto.ProductRequest;
import org.example.repository.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class RequestAnalyticsService {
    private final Repository<ProductRequest, UUID> requestRepository;
    private final Repository<Client, UUID> clientRepository;

    public RequestAnalyticsService(Repository<ProductRequest, UUID> requestRepository, Repository<Client, UUID> clientRepository) {
        this.requestRepository = requestRepository;
        this.clientRepository = clientRepository;
    }

    public double averageRequestAmount() {
        return requestRepository.findAll().stream()
                .mapToDouble(ProductRequest::getAmount)
                .average()
                .orElse(0.0);
    }

    public int maxTerm(){
        return requestRepository.findAll().stream()
                .map(ProductRequest::getTermMonths)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public Map<RequestStatus, Double> statusDistributionPercent() {
        List<ProductRequest> all = requestRepository.findAll();
        long total = all.size();
        if (total == 0) return Map.of();

        return all.stream()
                .collect(Collectors.groupingBy(ProductRequest::getStatus, Collectors.counting()))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (e.getValue() * 100.0) / total
                ));
    }

    public Map<String, Double> averageAmountByAgeCategory() {
        Map<UUID, Client> clientsById = clientRepository.findAll().stream()
                .collect(Collectors.toMap(Client::getId, c -> c));

        return requestRepository.findAll().stream()
                .filter(r -> clientsById.containsKey(r.getClientId()))
                .collect(Collectors.groupingBy(
                        r -> ageCategory(clientsById.get(r.getClientId()).getAge()),
                        Collectors.averagingDouble(ProductRequest::getAmount)
                ));
    }

    private String ageCategory(int age) {
        if (age < 25) return "18-24";
        if (age < 35) return "25-34";
        if (age < 45) return "35-44";
        if (age < 60) return "45-59";
        return "60+";
    }
}
