package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.VariableContext;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.tree.Rule;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Variable extends Expression {
    private final String name;
    private final VariableContext context;

    public Variable(VariableContext context, String name) {
        this.context = context;
        this.name = name;
    }

    @Override
    public Literal ev() {
        Rule variable = context.get(name);
        if (variable == null) {
            throw new CartoshkaException(String.format("Undefined variable: %s", name));
        }

        throw new NotImplementedException();
    }
}