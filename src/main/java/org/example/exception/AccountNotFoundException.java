package org.example.exception;

import java.util.UUID;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(UUID accountId) {
        super("Account with id=" + accountId + " not found");
    }
}