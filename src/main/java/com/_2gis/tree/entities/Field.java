package com._2gis.tree.entities;

import com._2gis.CartoshkaException;
import com._2gis.Feature;
import com._2gis.Location;

public class Field extends Expression {
    private final String name;

    public Field(Location location, String name) {
        super(location);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Literal ev(Feature feature) {
        if (feature == null) {
            throw CartoshkaException.featureIsNotProvided(getLocation());
        }

        Literal field = feature.getField(name);
        if (field == null) {
            throw CartoshkaException.fieldNotFound(getLocation());
        }

        return field;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public void fold() {
    }
}