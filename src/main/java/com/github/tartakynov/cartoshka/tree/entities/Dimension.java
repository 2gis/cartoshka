package com.github.tartakynov.cartoshka.tree.entities;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Dimension extends Expression {
    private final Number value;

    private final String unit;

    public Dimension(Number value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public Expression ev() {
        throw new NotImplementedException();
    }
}
