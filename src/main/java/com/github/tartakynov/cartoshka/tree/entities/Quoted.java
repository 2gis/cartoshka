package com.github.tartakynov.cartoshka.tree.entities;

public class Quoted extends Expression {
    private final String value;

    public Quoted(String value) {
        this.value = value;
    }
}
