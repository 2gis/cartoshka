package com.github.tartakynov.cartoshka.exceptions;

public class UnexpectedCharException extends CartoshkaException {
    public UnexpectedCharException(char character, int pos) {
        super(String.format("Unexpected character [%c] at position %d", character, pos));
    }

    public UnexpectedCharException(char character) {
        super(String.format("Unexpected character [%c]", character));
    }
}
