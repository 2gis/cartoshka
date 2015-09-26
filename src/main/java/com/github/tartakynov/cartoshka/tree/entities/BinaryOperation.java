package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.scanners.TokenType;

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
    public Literal ev() {
        return left.ev().operate(operator, right.ev());
    }
}