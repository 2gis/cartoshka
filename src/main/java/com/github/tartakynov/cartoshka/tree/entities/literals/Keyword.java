package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Keyword extends Literal {
    private final String value;

    public Keyword(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean isKeyword() {
        return true;
    }
}
