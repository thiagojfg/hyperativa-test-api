package com.hyperativa.exceptions;

public class DuplicatedCardNumberException extends RuntimeException {
    public DuplicatedCardNumberException(String cardNumber) {
        super(String.format("Card number %s already exists", cardNumber));
    }
}
