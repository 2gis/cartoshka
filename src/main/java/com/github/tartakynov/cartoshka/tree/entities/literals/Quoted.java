package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Quoted extends Literal {
    private final String value;

    public Quoted(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isQuoted() {
        return true;
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        if (operator == TokenType.ADD && (operand.isNumeric() || operand.isQuoted() || operand.isURL())) {
            return new Quoted(this.toString() + operand.toString());
        }

        return super.operate(operator, operand);
    }
}