package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.tree.entities.Expression;

import java.util.Collection;

public abstract class Node {
    protected static Expression fold(Expression expression) {
        if (!expression.isDynamic()) {
            return expression.ev(null);
        }

        expression.fold();
        return expression;
    }

    protected static void fold(Collection<? extends Node> nodes) {
        for (Node node : nodes) {
            node.fold();
        }
    }

    public abstract void fold();
}