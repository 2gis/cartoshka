package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Evaluable;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.tree.Node;

import java.util.Collection;

public abstract class Expression extends Node implements Evaluable<Literal> {
    public Expression(Location location) {
        super(location);
    }

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