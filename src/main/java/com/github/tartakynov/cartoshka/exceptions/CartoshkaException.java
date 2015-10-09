package com.github.tartakynov.cartoshka.exceptions;

import com.github.tartakynov.cartoshka.Location;
import com.github.tartakynov.cartoshka.scanner.Token;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class CartoshkaException extends RuntimeException {
    public CartoshkaException(String message) {
        super(message);
    }

    public CartoshkaException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CartoshkaException featureIsNotProvided(Location location) {
        throw new NotImplementedException();
    }

    public static CartoshkaException fieldNotFound(Location location) {
        throw new NotImplementedException();
    }

    public static CartoshkaException invalidDimensionUnit(Location location) {
        throw new NotImplementedException();
    }

    public static CartoshkaException invalidFormat(Location location) {
        throw new NotImplementedException();
    }

    public static CartoshkaException invalidOperation(Location location) {
        throw new NotImplementedException();
    }

    public static ClassCastException incorrectComparison(Location location) {
        return new ClassCastException("Incorrect comparison");
    }

    public static CartoshkaException functionTooManyArguments(Location location) {
        throw new NotImplementedException();
    }

    public static CartoshkaException functionIncorrectArgumentType(Location location) {
        throw new NotImplementedException();
    }

    public static CartoshkaException functionIncorrectArgumentCount(Location location, int expected, int actual) {
        throw new NotImplementedException();
    }

    public static CartoshkaException selectorWithoutSegments(Location location) {
        throw new NotImplementedException();
    }

    public static CartoshkaException undefinedName(Location location) {
        throw new NotImplementedException();
    }

    public static CartoshkaException unexpectedToken(Token token) {
        throw new NotImplementedException();
    }

    public static CartoshkaException unexpectedChar(Location location) {
        throw new NotImplementedException();
    }
}