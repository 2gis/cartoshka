package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;

import java.util.Iterator;

public class Arguments {
    public static Double numeric(Iterator<Literal> iter, String name, String func, boolean mapTo255) {
        Literal literal = iter.next();
        Double arg = literal.toNumber();
        if (arg == null) {
            throw ArgumentException.incorrectType(func, name);
        }

        return (literal.hasDot() && mapTo255) ? arg * 0xFF : arg;
    }

    public static Double percent(Iterator<Literal> iter, String name, String func) {
        Literal literal = iter.next();
        if (literal.isDimension()) {
            // only percent-unit dimension should return a numeric value
            return literal.toNumber();
        }

        return literal.toNumber() / 100.0;
    }

    public static Color color(Iterator<Literal> iter, String func, String name) {
        Literal arg = iter.next();
        if (arg == null || !arg.isColor()) {
            throw ArgumentException.incorrectType(func, name);
        }

        return (Color) arg;
    }
}