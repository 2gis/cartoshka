package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Boolean extends Literal {
    private final boolean value;

    public Boolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public String toString() {
        return this.value ? "true" : "false";
    }
}
