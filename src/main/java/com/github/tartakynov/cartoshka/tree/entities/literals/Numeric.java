package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Numeric extends Literal {
    private final double value;
    private final boolean hasDot;

    public Numeric(double value, boolean hasDot) {
        this.value = value;
        this.hasDot = hasDot;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Literal operate(TokenType operator) {
        return new Numeric(-value, hasDot);
    }

    @Override
    public boolean hasDot() {
        return hasDot;
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        if (operand.isNumeric()) {
            switch (operator) {
                case ADD:
                    return new Numeric(value + operand.toNumber(), hasDot || operand.hasDot());
                case SUB:
                    return new Numeric(value - operand.toNumber(), hasDot || operand.hasDot());
                case MUL:
                    return new Numeric(value * operand.toNumber(), hasDot || operand.hasDot());
                case DIV:
                    return new Numeric(value / operand.toNumber(), hasDot || operand.hasDot());
                case MOD:
                    return new Numeric(value % operand.toNumber(), hasDot || operand.hasDot());
            }
        } else if (operand.isText() && operator == TokenType.ADD) {
            return new Text(toString() + operand.toString(), false);
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
        if (hasDot) {
            return Double.toString(value);
        }

        return Integer.toString((int) value);
    }
}