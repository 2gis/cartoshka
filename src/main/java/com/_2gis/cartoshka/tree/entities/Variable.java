package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Context;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

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
        return visitor.visitVariableExpression(this, params);
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        if (value == null) {
            value = context.getVariable(name);
            if (value == null) {
                throw CartoshkaException.undefinedVariable(getLocation());
            }
        }

        return value;
    }
}