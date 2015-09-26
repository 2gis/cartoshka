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
            switch (operator) {
                case ADD:
                    return new Numeric(value + operand.toNumber());
                case SUB:
                    return new Numeric(value - operand.toNumber());
                case MUL:
                    return new Numeric(value * operand.toNumber());
                case DIV:
                    return new Numeric(value / operand.toNumber());
                case MOD:
                    return new Numeric(value % operand.toNumber());
            }
        }

        return super.operate(operator, operand);
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    public Double toNumber() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
