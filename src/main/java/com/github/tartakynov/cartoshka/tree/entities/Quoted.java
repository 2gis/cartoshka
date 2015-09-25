package com.github.tartakynov.cartoshka.tree.entities;

public class Quoted extends Expression {
    private final String value;

    public Quoted(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Expression ev() {
        return this;
    }
}
