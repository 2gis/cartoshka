package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.*;

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
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitVariable(this, params);
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