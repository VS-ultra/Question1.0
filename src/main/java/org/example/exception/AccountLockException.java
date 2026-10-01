package org.example.exception;

import java.util.UUID;

public class AccountLockException extends BusinessException {
    public AccountLockException(UUID accountId) {
        super("Could not lock account " + accountId + ": timeout");
    }

    public AccountLockException(UUID accountId, Throwable cause) {
        super("Could not lock account " + accountId + ": interrupted", cause);
    }
}