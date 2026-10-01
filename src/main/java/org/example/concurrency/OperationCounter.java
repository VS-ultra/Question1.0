package org.example.concurrency;

import org.example.ENUM.ProductType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;


public class OperationCounter {
    private final Map<ProductType, LongAdder> counters = new ConcurrentHashMap<>();

    public void increment(ProductType type){
        Objects.requireNonNull(type, "type cannot be null");
        counters.computeIfAbsent(type, t -> new LongAdder()).increment();
    }
    public long get(ProductType type){
        LongAdder adder = counters.get(type);
        return adder == null ? 0 : adder.sum();
    }
    public Map<ProductType, Long> snapshot() {
        Map<ProductType, Long> result = new EnumMap<>(ProductType.class);
        for (ProductType type : ProductType.values()) {
            result.put(type, get(type));
        }
        return result;
    }

}
