package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.CartoshkaException;
import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.Location;

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