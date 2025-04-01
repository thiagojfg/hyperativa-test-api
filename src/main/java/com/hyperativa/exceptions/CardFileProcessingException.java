package com.hyperativa.exceptions;

public class CardFileProcessingException extends RuntimeException {

    public CardFileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardFileProcessingException(String message) {
        super(message);
    }
}
