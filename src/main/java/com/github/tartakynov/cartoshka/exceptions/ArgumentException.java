package com.github.tartakynov.cartoshka.exceptions;

public class ArgumentException extends CartoshkaException {
    public ArgumentException(String function, int expected, int given) {
        super(String.format("Incorrect number of arguments for function %s, expected %d but given %d", function, expected, given));
    }

    public ArgumentException(String function, String argument) {
        super(String.format("Incorrect type of argument %s for function %s", argument, function));
    }
}
