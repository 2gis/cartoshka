package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;

import java.util.Iterator;

public class Arguments {
    public static Double numeric(Iterator<Expression> iter, String name, String func, boolean mapTo255) {
        Literal literal = iter.next().ev();
        Double arg = literal.toNumber();
        if (arg == null) {
            throw ArgumentException.incorrectType(func, name);
        }

        return (literal.hasDot() && mapTo255) ? arg * 0xFF : arg;
    }

    public static Color color(Iterator<Expression> iter, String func, String name) {
        Literal arg = iter.next().ev();
        if (arg == null || !arg.isColor()) {
            throw ArgumentException.incorrectType(func, name);
        }

        return (Color) arg;
    }
}
