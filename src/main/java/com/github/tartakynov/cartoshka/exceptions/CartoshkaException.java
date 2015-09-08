package com.github.tartakynov.cartoshka.exceptions;

public abstract class CartoshkaException extends RuntimeException {
    protected CartoshkaException(String message) {
        super(message);
    }

    protected CartoshkaException(String message, Throwable cause) {
        super(message, cause);
    }
}
