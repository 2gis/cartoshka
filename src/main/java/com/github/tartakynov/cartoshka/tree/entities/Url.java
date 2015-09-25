package com.github.tartakynov.cartoshka.tree.entities;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Url extends Expression {
    private final String value;

    public Url(String value) {
        this.value = value;
    }

    @Override
    public Expression ev() {
        throw new NotImplementedException();
    }
}
