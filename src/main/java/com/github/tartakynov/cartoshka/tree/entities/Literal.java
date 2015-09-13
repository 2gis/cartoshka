package com.github.tartakynov.cartoshka.tree.entities;

public class Literal extends Expression {
    private final String value;

    public Literal(String value) {
        this.value = value;
    }
}
