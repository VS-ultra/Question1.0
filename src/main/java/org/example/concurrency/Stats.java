package org.example.concurrency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Stats(long count, BigDecimal sum, BigDecimal min, BigDecimal max) {

    public static final Stats EMPTY = new Stats(0, BigDecimal.ZERO, null, null);

    public Stats add(BigDecimal amount) {
        BigDecimal newMin = (min == null || amount.compareTo(min) < 0) ? amount : min;
        BigDecimal newMax = (max == null || amount.compareTo(max) > 0) ? amount : max;
        return new Stats(count + 1, sum.add(amount), newMin, newMax);
    }

    public BigDecimal average() {
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }
}