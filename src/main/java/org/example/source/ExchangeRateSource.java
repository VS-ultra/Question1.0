package org.example.source;


import java.math.BigDecimal;


public interface ExchangeRateSource {

    String getName();
    BigDecimal getRate(String currency);
}