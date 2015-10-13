package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Evaluable;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.tree.Node;

public abstract class Expression extends Node implements Evaluable<Literal> {
    public Expression(Location location) {
        super(location);
    }

    /**
     * Evaluates the expression.
     *
     * @return A literal that results from the evaluation of the expression
     */
    public abstract Literal ev(Feature feature);

    public boolean isLiteral() {
        return false;
    }
}