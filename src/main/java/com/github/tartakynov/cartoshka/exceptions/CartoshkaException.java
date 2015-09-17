package com.github.tartakynov.cartoshka.exceptions;

public class CartoshkaException extends RuntimeException {
    public CartoshkaException(String message) {
        super(message);
    }

    public CartoshkaException(String message, Throwable cause) {
        super(message, cause);
    }
}
