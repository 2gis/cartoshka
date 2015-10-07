package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;

public class Field extends Expression {
    private final String name;

    public Field(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Literal ev(Feature feature) {
        if (feature == null) {
            throw new CartoshkaException(String.format("Feature is not provided to field: %s", name));
        }

        Literal field = feature.getField(name);
        if (field == null) {
            throw new CartoshkaException(String.format("Field not found: %s", name));
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