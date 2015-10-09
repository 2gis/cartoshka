package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.scanner.Token;

public class CartoshkaException extends RuntimeException {
    private final Location location;

    public CartoshkaException(Location location, String format, Object... args) {
        super(String.format("%s. Line %d position %d", String.format(format, args), location.line, location.linePos));
        this.location = location;
    }

    public static CartoshkaException featureIsNotProvided(Location location) {
        throw new CartoshkaException(location, "Unable to evaluate the expression without feature");
    }

    public static CartoshkaException fieldNotFound(Location location) {
        throw new CartoshkaException(location, "Field not found");
    }

    public static CartoshkaException invalidDimensionUnit(Location location) {
        throw new CartoshkaException(location, "Invalid dimension unit");
    }

    public static CartoshkaException invalidFormat(Location location) {
        throw new CartoshkaException(location, "Invalid format");
    }

    public static CartoshkaException invalidOperation(Location location) {
        throw new CartoshkaException(location, "Invalid operation");
    }

    public static ClassCastException incorrectComparison(Location location) {
        return new ClassCastException(String.format("Incorrect comparison at line %d position %d", location.line, location.linePos));
    }

    public static CartoshkaException functionTooManyArguments(Location location) {
        throw new CartoshkaException(location, "Too many arguments");
    }

    public static CartoshkaException functionIncorrectArgumentType(Location location) {
        throw new CartoshkaException(location, "Incorrect type");
    }

    public static CartoshkaException functionIncorrectArgumentCount(Location location, int expected, int actual) {
        throw new CartoshkaException(location, "Incorrect arguments count, expected %d but given %d", expected, actual);
    }

    public static CartoshkaException selectorWithoutSegments(Location location) {
        throw new CartoshkaException(location, "Selector without segments");
    }

    public static CartoshkaException undefinedFunction(Location location) {
        throw new CartoshkaException(location, "Undefined name");
    }

    public static CartoshkaException undefinedVariable(Location location) {
        throw new CartoshkaException(location, "Undefined variable");
    }

    public static CartoshkaException unexpectedToken(Token token) {
        throw new CartoshkaException(token.getLocation(), "Unexpected token %s", token.getType().name());
    }

    public static CartoshkaException unexpectedChar(Location location, char c) {
        throw new CartoshkaException(location, "Unexpected char %c", c);
    }

    public Location getLocation() {
        return location;
    }
}