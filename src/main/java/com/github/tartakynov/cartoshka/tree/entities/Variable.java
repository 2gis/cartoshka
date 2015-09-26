package com.github.tartakynov.cartoshka.tree.entities;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public Literal ev() {
        throw new NotImplementedException();
    }
}
