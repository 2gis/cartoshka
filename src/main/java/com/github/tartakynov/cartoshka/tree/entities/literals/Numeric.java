package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Numeric extends Literal {
    private final double value;

    public Numeric(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Literal operate(TokenType operator) {
        return new Numeric(-value);
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        if (operand.isNumeric()) {
            return new Numeric(value + operand.asNumber());
        }

        return super.operate(operator, operand);
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    public Double asNumber() {
        return value;
    }
}
