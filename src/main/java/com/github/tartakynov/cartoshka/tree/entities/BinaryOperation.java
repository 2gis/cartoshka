package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BinaryOperation extends Expression {
    private final TokenType operator;

    private final Expression left;

    private final Expression right;

    public BinaryOperation(TokenType operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public Expression ev() {
        Expression leftOp = left.ev();
        Expression rightOp = right.ev();

        switch (operator) {
            case ADD:
                break;
            case SUB:
                break;
            case MUL:
                break;
            case DIV:
                break;
            case MOD:
                break;
        }

        throw new NotImplementedException();
    }
}
