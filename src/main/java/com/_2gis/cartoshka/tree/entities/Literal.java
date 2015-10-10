package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.scanner.TokenType;

public abstract class Literal extends Expression implements Comparable<Literal> {
    public Literal(Location location) {
        super(location);
    }

    @Override
    public Literal ev(Feature feature) {
        return this;
    }

    public Literal operate(TokenType operator, Literal operand) {
        throw CartoshkaException.invalidOperation(getLocation());
    }

    public Literal operate(TokenType operator) {
        throw CartoshkaException.invalidOperation(getLocation());
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isColor() {
        return false;
    }

    public boolean isDimension() {
        return false;
    }

    public boolean isKeyword() {
        return false;
    }

    public boolean isNumeric() {
        return false;
    }

    public boolean isImageFilter() {
        return false;
    }

    public boolean isText() {
        return false;
    }

    public boolean hasDot() {
        return false;
    }

    public boolean isMulti() {
        return false;
    }

    public Double toNumber() {
        return null;
    }

    @Override
    public abstract String toString();

    public boolean isURL() {
        return false;
    }

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public void fold() {
    }
}