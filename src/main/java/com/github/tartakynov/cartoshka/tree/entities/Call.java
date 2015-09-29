package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Function;

import java.util.Collection;

public class Call extends Expression {
    private final Function function;
    private final Collection<Expression> args;

    public Call(Function function, Collection<Expression> args) {
        this.function = function;
        this.args = args;
    }

    @Override
    public Literal ev() {
        return function.apply(args.iterator());
    }
}