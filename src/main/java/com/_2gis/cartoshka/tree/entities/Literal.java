package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.scanner.TokenType;

public abstract class Literal extends Expression implements Comparable<Literal> {
    public Literal(Location location) {
        super(location);
    }

    public Literal operate(TokenType operator, Literal operand) {
        throw CartoshkaException.invalidOperation(getLocation());
    }

    public Literal operate(TokenType operator) {
        throw CartoshkaException.invalidOperation(getLocation());
    }

    public boolean hasDot() {
        return false;
    }

    public Double toNumber() {
        return null;
    }

    @Override
    public abstract String toString();
}