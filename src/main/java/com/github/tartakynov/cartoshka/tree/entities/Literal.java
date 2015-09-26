package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.exceptions.OperationException;
import com.github.tartakynov.cartoshka.scanners.TokenType;

public abstract class Literal extends Expression {
    @Override
    public Literal ev() {
        return this;
    }

    public abstract Literal operate(TokenType operator, Literal operand);

    public Literal operate(TokenType operator) {
        String operandType = this.getClass().getSimpleName().toLowerCase();
        throw new OperationException("Operator [-] cannot be applied to " + operandType);
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

    public boolean isURL() {
        return false;
    }
}
