package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;

import java.util.Iterator;

public class Arguments {
    protected static Double numeric(Iterator<Expression> iter, String func, String name, boolean mapTo255) {
        if (!iter.hasNext()) {
            throw ArgumentException.notEnough(func);
        }

        Literal literal = iter.next().ev();
        Double arg = literal.toNumber();
        if (arg == null) {
            throw ArgumentException.incorrectType(func, name);
        }

        return (literal.hasDot() && mapTo255) ? arg * 0xFF : arg;
    }

    protected static Color color(Iterator<Expression> iter, String func, String name) {
        if (!iter.hasNext()) {
            throw ArgumentException.notEnough(func);
        }

        Literal arg = iter.next().ev();
        if (arg == null || !arg.isColor()) {
            throw ArgumentException.incorrectType(func, name);
        }

        return (Color) arg;
    }
}
