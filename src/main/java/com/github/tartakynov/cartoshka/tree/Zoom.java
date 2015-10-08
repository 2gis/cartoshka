package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Expression;

public class Zoom extends Node {
    private final TokenType operator;
    private Expression expression;

    public Zoom(TokenType operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void fold() {
        expression = fold(expression);
    }
}