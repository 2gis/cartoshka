package com._2gis.cartoshka.function;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.tree.expression.Literal;
import com._2gis.cartoshka.tree.expression.literal.Color;

import java.util.Iterator;

public class Arguments {
    public static Double numeric(Iterator<Literal> args, boolean mapTo255) {
        Literal literal = args.next();
        Double arg = literal.toNumber();
        if (arg == null) {
            throw CartoshkaException.functionIncorrectArgumentType(literal.getLocation());
        }

        return ((literal.hasDot() || literal.type() == NodeType.DIMENSION) && mapTo255) ? arg * 0xFF : arg;
    }

    public static Double percent(Iterator<Literal> args) {
        Literal literal = args.next();
        if (literal.type() == NodeType.DIMENSION) {
            // only percent-unit dimension should return a numeric value
            return literal.toNumber();
        }

        return literal.toNumber() / 100.0;
    }

    public static Color color(Iterator<Literal> args) {
        Literal arg = args.next();
        if (arg.type() != NodeType.COLOR) {
            throw CartoshkaException.functionIncorrectArgumentType(arg.getLocation());
        }

        return (Color) arg;
    }
}