package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.exceptions.OperationException;
import com.github.tartakynov.cartoshka.scanners.TokenType;

public abstract class Literal extends Expression {
    @Override
    public Literal ev() {
        return this;
    }

    public Literal operate(TokenType operator, Literal operand) {
        String leftType = this.getClass().getSimpleName().toLowerCase();
        String rightType = operand.getClass().getSimpleName().toLowerCase();
        throw new OperationException(String.format("Operator [%s] cannot be applied to %s and %s", operator.getStr(), leftType, rightType));
    }

    public Literal operate(TokenType operator) {
        String operandType = this.getClass().getSimpleName().toLowerCase();
        throw new OperationException(String.format("Operator [%s] cannot be applied to %s", operator.getStr(), operandType));
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isColor() {
        return false;
    }

    public boolean isDimension() {
        return false;
    }

    public boolean isField() {
        return false;
    }

    public boolean isKeyword() {
        return false;
    }

    public boolean isNumeric() {
        return false;
    }

    public boolean isQuoted() {
        return false;
    }

    public Double toNumber() {
        return null;
    }

    @Override
    public abstract String toString();

    public boolean isURL() {
        return false;
    }
}
