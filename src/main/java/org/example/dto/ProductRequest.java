package org.example.dto;

import lombok.Getter;
import org.example.ENUM.RequestStatus;
import org.example.repository.Identifiable;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ProductRequest implements Identifiable<UUID> {
    //main fields
    private final UUID id;
    private final UUID clientId;
    private final UUID productId;
    private final Double amount;
    // second fields
    private final Integer termMonths;
    private final RequestStatus status;
    private final LocalDateTime createdAt;

    private ProductRequest(Builder builder) {
        this.id = builder.id;
        this.clientId = builder.clientId;
        this.productId = builder.productId;
        this.amount = builder.amount;
        this.termMonths = builder.termMonths;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
    }
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID clientId;
        private UUID productId;
        private Double amount;

        private Integer termMonths;
        private RequestStatus status = RequestStatus.NEW;
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder clientId(UUID clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder termMonths(Integer termMonths) {
            this.termMonths = termMonths;
            return this;
        }

        public Builder status(RequestStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductRequest build() {
            if (id == null) {
                throw new IllegalArgumentException("id cannot be null");
            }
            if (clientId == null) {
                throw new IllegalArgumentException("clientId cannot be null");
            }
            if (productId == null) {
                throw new IllegalArgumentException("productId cannot be null");
            }
            if (amount == null) {
                throw new IllegalArgumentException("amount cannot be null");
            }
            return new ProductRequest(this);
        }
    }
    public ProductRequest withStatus(RequestStatus newStatus){
        return builder()
                .id(id)
                .clientId(clientId)
                .productId(productId)
                .amount(amount)
                .termMonths(termMonths)
                .status(newStatus)
                .createdAt(createdAt)
                .build();
    }
}
