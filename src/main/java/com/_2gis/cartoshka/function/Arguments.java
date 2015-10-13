package com._2gis.cartoshka.function;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.tree.entities.Literal;
import com._2gis.cartoshka.tree.entities.literals.Color;

import java.util.Iterator;

public class Arguments {
    public static Double numeric(Iterator<Literal> iter, boolean mapTo255) {
        Literal literal = iter.next();
        Double arg = literal.toNumber();
        if (arg == null) {
            throw CartoshkaException.functionIncorrectArgumentType(literal.getLocation());
        }

        return ((literal.hasDot() || literal.isDimension()) && mapTo255) ? arg * 0xFF : arg;
    }

    public static Double percent(Iterator<Literal> iter) {
        Literal literal = iter.next();
        if (literal.isDimension()) {
            // only percent-unit dimension should return a numeric value
            return literal.toNumber();
        }

        return literal.toNumber() / 100.0;
    }

    public static Color color(Iterator<Literal> iter) {
        Literal arg = iter.next();
        if (!arg.isColor()) {
            throw CartoshkaException.functionIncorrectArgumentType(arg.getLocation());
        }

        return (Color) arg;
    }
}