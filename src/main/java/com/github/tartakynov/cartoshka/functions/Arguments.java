package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Arguments {
    public static Double numeric(Literal literal, String func, String name, boolean mapTo255) {
        Double arg = literal.toNumber();
        if (arg == null) {
            throw new ArgumentException(func, name);
        }

        return (literal.hasDot() && mapTo255) ? arg * 0xFF : arg;
    }
}
