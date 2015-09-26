package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Url extends Literal {
    private final String value;

    public Url(String value) {
        this.value = value;
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        return null;
    }

    @Override
    public boolean isURL() {
        return true;
    }
}
