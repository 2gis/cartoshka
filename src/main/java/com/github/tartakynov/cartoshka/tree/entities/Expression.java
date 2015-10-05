package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.tree.Node;

import java.util.Collection;

public abstract class Expression extends Node {
    protected static boolean hasDynamicExpression(Collection<Expression> args) {
        for (Expression arg : args) {
            if (arg.isDynamic()) {
                return true;
            }
        }

        return false;
    }

    public abstract Literal ev();

    public abstract boolean isDynamic();
}
