package org.example.concurrency.config;


import java.util.Objects;

public record ThreadPoolConfig(int poolSize, String threadNamePrefix, long shutdownTimeoutSeconds){
    public ThreadPoolConfig {
        if (poolSize <= 0) {
            throw new IllegalArgumentException("poolSize must be > 0");
        }
        Objects.requireNonNull(threadNamePrefix, "threadNamePrefix cannot be null");
        if (shutdownTimeoutSeconds < 0) {
            throw new IllegalArgumentException("shutdownTimeoutSeconds must be >= 0");
        }
    }
    public static ThreadPoolConfig defaults() {
        return new ThreadPoolConfig(Runtime.getRuntime().availableProcessors(), "bank-worker", 10);
    }
}

