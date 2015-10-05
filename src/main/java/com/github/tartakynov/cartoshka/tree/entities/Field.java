package com.github.tartakynov.cartoshka.tree.entities;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Field extends Expression {
    private final String name;

    public Field(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Literal ev() {
        throw new NotImplementedException();
    }
}