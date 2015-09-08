package com.github.tartakynov.cartoshka.exceptions;

public class UnexpectedTokenException extends CartoshkaException {
    public UnexpectedTokenException(String token, int pos) {
        super(String.format("Unexpected token [%s] at position %d", token, pos));
    }

    public UnexpectedTokenException(String token) {
        super(String.format("Unexpected token [%s]", token));
    }
}
