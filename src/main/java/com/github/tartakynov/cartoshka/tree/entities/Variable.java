package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Context;
import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;

public class Variable extends Expression {
    private final String name;
    private final Context context;

    public Variable(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    @Override
    public Literal ev(Feature feature) {
        return getValue().ev(feature);
    }

    @Override
    public boolean isDynamic() {
        return getValue().isDynamic();
    }

    private Value getValue() {
        Value value = context.getVariable(name);
        if (value == null) {
            throw new CartoshkaException(String.format("Undefined variable: %s", name));
        }

        return value;
    }
}