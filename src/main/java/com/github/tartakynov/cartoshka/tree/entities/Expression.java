package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.tree.Node;

import java.util.Collection;

public abstract class Expression extends Node {
    protected static boolean hasDynamicExpression(Collection<? extends Expression> args) {
        for (Expression arg : args) {
            if (arg.isDynamic()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Evaluates the expression.
     *
     * @return A literal that results from the evaluation of the expression
     */
    public abstract Literal ev(Feature feature);

    /**
     * Checks if the expression is dynamic.
     * An expression is dynamic if its value can be evaluated only at runtime.
     *
     * @return true if the expression is dynamic
     */
    public abstract boolean isDynamic();

    public boolean isLiteral() {
        return false;
    }
}