package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.scanners.TokenType;

public class CompareOperation extends Expression {
    private final TokenType operator;

    private final Expression left;

    private final Expression right;

    public CompareOperation(TokenType operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}