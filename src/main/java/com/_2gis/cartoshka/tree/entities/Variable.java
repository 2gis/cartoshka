package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Context;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;

public class Variable extends Expression {
    private final String name;
    private final Context context;
    private Value value;

    public Variable(Location location, Context context, String name) {
        super(location);
        this.context = context;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Literal ev(Feature feature) {
        return getValue().ev(feature);
    }

    @Override
    public boolean isDynamic() {
        return getValue().isDynamic();
    }

    private synchronized Value getValue() {
        if (value == null) {
            value = context.getVariable(name);
            if (value == null) {
                throw CartoshkaException.undefinedVariable(getLocation());
            }
        }

        return value;
    }

    @Override
    public void fold() {
        getValue().fold();
    }
}