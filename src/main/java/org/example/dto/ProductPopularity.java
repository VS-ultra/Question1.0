package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductPopularity {
    private AbstractFinProduct product;
    private long requestCount;
    private double approvalRate;
}
