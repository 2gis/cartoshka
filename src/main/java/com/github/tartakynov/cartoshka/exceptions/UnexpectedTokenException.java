package com.github.tartakynov.cartoshka.exceptions;

public class UnexpectedTokenException extends CartoshkaException {
    public UnexpectedTokenException(String found, int pos) {
        super(String.format("Unexpected token %s at position %d", found, pos));
    }

    public UnexpectedTokenException(String found) {
        super(String.format("Unexpected token %s", found));
    }
}
