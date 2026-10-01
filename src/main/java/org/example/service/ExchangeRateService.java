package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.source.ExchangeRateSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class ExchangeRateService {

    private final List<ExchangeRateSource> sources;
    private final long timeoutMs;
    private final ExecutorService executor;
    private final Map<String, BigDecimal> lastKnownRates = new ConcurrentHashMap<>();

    public ExchangeRateService(List<ExchangeRateSource> sources, long timeoutMs) {
        if (sources == null || sources.isEmpty()) {
            throw new IllegalArgumentException("At least one source is required");
        }
        this.sources = List.copyOf(sources);
        this.timeoutMs = timeoutMs;
        this.executor = Executors.newFixedThreadPool(sources.size());
    }

    public CompletableFuture<BigDecimal> getRate(String currency) {
        Objects.requireNonNull(currency, "currency cannot be null");

        List<CompletableFuture<BigDecimal>> requests = sources.stream()
                .map(source -> requestRate(source, currency))
                .toList();
        return CompletableFuture.allOf(requests.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> combine(currency, requests));
    }

    public void shutdown() {
        executor.shutdownNow();
    }

    private CompletableFuture<BigDecimal> requestRate(ExchangeRateSource source, String currency) {
        return CompletableFuture
                .supplyAsync(() -> source.getRate(currency), executor)
                .orTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .exceptionally(error -> {
                    log.warn("Source {} failed for {}: {}", source.getName(), currency, error.toString());
                    return null;
                });
    }

    private BigDecimal combine(String currency, List<CompletableFuture<BigDecimal>> requests) {
        List<BigDecimal> rates = requests.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();

        if (rates.isEmpty()) {
            return fallback(currency);
        }

        BigDecimal sum = rates.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal average = sum.divide(BigDecimal.valueOf(rates.size()), 4, RoundingMode.HALF_UP);

        lastKnownRates.put(currency, average);
        log.info("Rate {}: {} (from {} of {} sources)", currency, average, rates.size(), sources.size());
        return average;
    }

    private BigDecimal fallback(String currency) {
        BigDecimal cached = lastKnownRates.get(currency);
        if (cached == null) {
            throw new IllegalStateException("No source available and no cached rate for " + currency);
        }
        log.warn("All sources failed, using last known rate for {}: {}", currency, cached);
        return cached;
    }
}