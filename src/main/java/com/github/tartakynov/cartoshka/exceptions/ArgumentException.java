package com.github.tartakynov.cartoshka.exceptions;

import java.util.Formatter;

public class ArgumentException extends CartoshkaException {
    private ArgumentException(String format, Object... args) {
        super(new Formatter().format(format, args).toString());
    }

    public static ArgumentException incorrectType(String func, String arg) {
        return new ArgumentException("Incorrect type of argument %s for function %s", arg, func);
    }

    public static ArgumentException notEnough(String func) {
        return new ArgumentException("Not enough arguments for function %s", func);
    }
}