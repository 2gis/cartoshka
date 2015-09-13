package com.github.tartakynov.cartoshka.tree.entities;

import java.util.Collection;

public class Call extends Expression {
    private final String function;
    private final Collection<Expression> args;

    public Call(String function, Collection<Expression> args) {
        this.function = function;
        this.args = args;
    }
}
