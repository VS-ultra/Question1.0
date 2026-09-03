package org.example.exception;

import java.util.UUID;

public class ProductNotActiveException extends BusinessException {
  public ProductNotActiveException(UUID id) {
    super("Product with id = " + id + " is not active");
  }
}
