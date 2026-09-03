package org.example.exception;

public abstract class BusinessException extends  Exception {
    public  BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
