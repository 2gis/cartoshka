package com.github.tartakynov.cartoshka.exceptions;

import com.github.tartakynov.cartoshka.tree.Node;

public class CartoshkaException extends RuntimeException {
    public CartoshkaException(String message) {
        super(message);
    }

    public CartoshkaException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ClassCastException incorrectComparison(Node node) {
        return new ClassCastException("Incorrect comparison");
    }

    public static CartoshkaException unexpectedChar(char character, int pos) {
        return new CartoshkaException(String.format("Unexpected character [%c] at position %d", character, pos));
    }

    public static CartoshkaException unexpectedToken(String found, int pos) {
        return new CartoshkaException(String.format("Unexpected token %s at position %d", found, pos));
    }

    public static CartoshkaException incorrectArgumentType(String func, String arg) {
        return new CartoshkaException(String.format("Incorrect type of argument %s for function %s", arg, func));
    }

    public static CartoshkaException incorrectArgumentCount(String func, int expected, int actual) {
        return new CartoshkaException(String.format("Wrong argument count for \"%s\", expected %d but given %d", func, expected, actual));
    }
}
