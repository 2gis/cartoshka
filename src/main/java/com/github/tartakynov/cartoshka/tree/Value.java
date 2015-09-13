package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.tree.entities.Expression;

import java.util.Collection;

public class Value extends Node {
    private final Collection<Expression> expressions;

    public Value(Collection<Expression> expressions) {
        this.expressions = expressions;
    }
}
