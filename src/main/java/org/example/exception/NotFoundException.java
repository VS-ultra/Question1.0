package org.example.exception;

import java.util.UUID;

public class NotFoundException extends  BusinessException {
    public NotFoundException(UUID productId) {
        super("Продукт с id=" + productId + " не найден");
    }
}
