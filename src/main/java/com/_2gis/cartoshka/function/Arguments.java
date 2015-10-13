package com._2gis.cartoshka.function;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.tree.entities.Literal;
import com._2gis.cartoshka.tree.entities.literals.Color;

import java.util.Iterator;

public class Arguments {
    public static Double numeric(Iterator<Literal> args, boolean mapTo255) {
        Literal literal = args.next();
        Double arg = literal.toNumber();
        if (arg == null) {
            throw CartoshkaException.functionIncorrectArgumentType(literal.getLocation());
        }

        return ((literal.hasDot() || literal.isDimension()) && mapTo255) ? arg * 0xFF : arg;
    }

    public static Double percent(Iterator<Literal> args) {
        Literal literal = args.next();
        if (literal.isDimension()) {
            // only percent-unit dimension should return a numeric value
            return literal.toNumber();
        }

        return literal.toNumber() / 100.0;
    }

    public static Color color(Iterator<Literal> args) {
        Literal arg = args.next();
        if (!arg.isColor()) {
            throw CartoshkaException.functionIncorrectArgumentType(arg.getLocation());
        }

        return (Color) arg;
    }
}