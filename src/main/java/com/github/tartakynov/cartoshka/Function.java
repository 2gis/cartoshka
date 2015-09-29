package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;

import java.util.Iterator;

public abstract class Function {
    protected final String name;

    protected Function(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Literal apply(Iterator<Expression> args);

    protected Double argNumeric(Iterator<Expression> iter, String func, boolean mapTo255) {
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

    protected Color argColor(Iterator<Expression> iter, String func) {
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