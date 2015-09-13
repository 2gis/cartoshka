package com.github.tartakynov.cartoshka.tree;

public class VariableDeclaration extends Node {
    private final Value value;

    public VariableDeclaration(String name, Value value) {
        this.value = value;
    }
}
