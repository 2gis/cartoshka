package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Url extends Literal {
    private final String value;

    public Url(String value) {
        this.value = value;
    }

    @Override
    public boolean isURL() {
        return true;
    }

    @Override
    public String asString() {
        return value;
    }
}
