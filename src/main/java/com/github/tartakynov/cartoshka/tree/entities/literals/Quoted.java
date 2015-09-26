package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.scanners.TokenType;
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
    public Literal operate(TokenType operator, Literal operand) {
        return null;
    }

    @Override
    public boolean isQuoted() {
        return true;
    }


}
