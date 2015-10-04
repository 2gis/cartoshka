package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.tree.entities.Literal;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Field extends Literal {
    private final String name;

    public Field(String name) {
        this.name = name;
    }

    @Override
    public boolean isField() {
        return true;
    }

    @Override
    public String toString() {
        throw new NotImplementedException();
    }
}
