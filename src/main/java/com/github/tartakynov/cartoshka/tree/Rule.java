package com.github.tartakynov.cartoshka.tree;

public class Rule extends Node {
    private final String instance;
    private final String name;
    private final Value value;
    private final boolean isVariable;

    public Rule(String name, Value value, boolean isVariable) {
        String[] parts = name.split("/");
        this.instance = parts.length == 1 ? "__default__" : parts[0];
        this.name = parts.length == 1 ? name : parts[1];
        this.value = value;
        this.isVariable = isVariable;
    }

    public String getInstance() {
        return instance;
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