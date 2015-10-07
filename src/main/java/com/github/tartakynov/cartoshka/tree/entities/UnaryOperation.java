package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.scanners.TokenType;

public class UnaryOperation extends Expression {
    private final TokenType operator;

    private Expression expression;

    public UnaryOperation(TokenType operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public Literal ev(Feature feature) {
        return expression.ev(feature).operate(operator);
    }

    @Override
    public boolean isDynamic() {
        return expression.isDynamic();
    }

    @Override
    public void fold() {
        expression = fold(expression);
    }
}