package org.example.concurrency;

import org.example.ENUM.ProductType;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


public class OperationStatistics {

    private final Map<ProductType, AtomicReference<Stats>> stats = new ConcurrentHashMap<>();

    public void record(ProductType type, BigDecimal amount) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(amount, "amount cannot be null");
        stats.computeIfAbsent(type, t -> new AtomicReference<>(Stats.EMPTY))
                .updateAndGet(current -> current.add(amount));
    }

    public Stats get(ProductType type) {
        AtomicReference<Stats> ref = stats.get(type);
        return ref == null ? Stats.EMPTY : ref.get();
    }

    public Map<ProductType, Stats> snapshot() {
        Map<ProductType, Stats> result = new EnumMap<>(ProductType.class);
        for (ProductType type : ProductType.values()) {
            result.put(type, get(type));
        }
        return result;
    }
}