package com.github.tartakynov.cartoshka.exceptions;

public class UnexpectedTokenException extends CartoshkaException {
    public UnexpectedTokenException(String expected, String found, int pos) {
        super(String.format("Expected %s but found %s at position %d", expected, found, pos));
    }

    public UnexpectedTokenException(String expected, String found) {
        super(String.format("Expected %s but found %s", expected, found));
    }
}
