package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.VariableContext;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;

public class Variable extends Expression {
    private final String name;
    private final VariableContext context;

    public Variable(VariableContext context, String name) {
        this.context = context;
        this.name = name;
    }

    @Override
    public Literal ev() {
        Value value = context.get(name);
        if (value == null) {
            throw new CartoshkaException(String.format("Undefined variable: %s", name));
        }

        return value.ev();
    }
}