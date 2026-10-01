package org.example.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class FakeExchangeRateSource implements ExchangeRateSource {

    @Getter
    private final String name;
    private final BigDecimal rate;
    private final long delayMs;

    @Setter
    private volatile boolean available = true;

    @Override
    public BigDecimal getRate(String currency) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(name + " interrupted", e);
        }
        if (!available) {
            throw new IllegalStateException(name + " is unavailable");
        }
        return rate;
    }
}