package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.scanners.TokenType;

public class UnaryOperation extends Expression {
    private final TokenType operator;

    private final Expression expression;

    public UnaryOperation(TokenType operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public Literal ev() {
        return expression.ev().operate(operator);
    }
}