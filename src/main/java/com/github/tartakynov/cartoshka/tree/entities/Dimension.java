package com.github.tartakynov.cartoshka.tree.entities;

public class Dimension extends Expression {
    private final Number value;

    private final String unit;

    public Dimension(Number value, String unit) {
        this.value = value;
        this.unit = unit;
    }
}
