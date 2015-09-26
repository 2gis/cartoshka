package com.github.tartakynov.cartoshka.tree;

public class Rule extends Node {
    private final String name;
    private final Value value;
    private final boolean isVariable;

    public Rule(String name, Value value, boolean isVariable) {
        this.name = name;
        this.value = value;
        this.isVariable = isVariable;
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public boolean isVariable() {
        return isVariable;
    }
}
