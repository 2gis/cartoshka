package com._2gis.cartoshka;

import com._2gis.cartoshka.scanner.Token;

import java.io.IOException;

public class CartoshkaException extends RuntimeException {
    private final Location location;

    public CartoshkaException(Location location, String format, Object... args) {
        super(String.format("%s line %d position %d: %s", location.name, location.line, location.linePos, String.format(format, args)));
        this.location = location;
    }

    public CartoshkaException(Throwable cause, Location location, String format, Object... args) {
        super(String.format("%s line %d position %d: %s", location.name, location.line, location.linePos, String.format(format, args)), cause);
        this.location = location;
    }

    public static CartoshkaException featureIsNotProvided(Location location) {
        return new CartoshkaException(location, "Unable to evaluate the expression without feature");
    }

    public static CartoshkaException fieldNotFound(Location location) {
        return new CartoshkaException(location, "Field not found");
    }

    public static CartoshkaException invalidDimensionUnit(Location location) {
        return new CartoshkaException(location, "Invalid dimension unit");
    }

    public static CartoshkaException invalidFormat(Location location) {
        return new CartoshkaException(location, "Invalid format");
    }

    public static CartoshkaException invalidOperation(Location location) {
        return new CartoshkaException(location, "Invalid operation");
    }

    public static ClassCastException incorrectComparison(Location location) {
        return new ClassCastException(String.format("Incorrect comparison at line %d position %d", location.line, location.linePos));
    }

    public static CartoshkaException functionTooManyArguments(Location location) {
        return new CartoshkaException(location, "Too many arguments");
    }

    public static CartoshkaException functionIncorrectArgumentType(Location location) {
        return new CartoshkaException(location, "Incorrect type");
    }

    public static CartoshkaException functionIncorrectArgumentCount(Location location, int expected, int actual) {
        return new CartoshkaException(location, "Incorrect arguments count, expected %d but given %d", expected, actual);
    }

    public static CartoshkaException selectorWithoutSegments(Location location) {
        return new CartoshkaException(location, "Selector without segments");
    }

    public static CartoshkaException undefinedFunction(Location location) {
        return new CartoshkaException(location, "Undefined name");
    }

    public static CartoshkaException undefinedVariable(Location location) {
        return new CartoshkaException(location, "Undefined variable");
    }

    public static CartoshkaException unexpectedToken(Token token) {
        return new CartoshkaException(token.getLocation(), "Unexpected token %s", token.getType().name());
    }

    public static CartoshkaException ioException(Location location, IOException exception) {
        return new CartoshkaException(exception, location, "IOException: %s", exception.getMessage());
    }

    public Location getLocation() {
        return location;
    }
}