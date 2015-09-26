package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Quoted extends Literal {
    private final String value;

    public Quoted(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean isQuoted() {
        return true;
    }
}
