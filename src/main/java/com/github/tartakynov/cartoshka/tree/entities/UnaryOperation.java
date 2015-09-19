package com.github.tartakynov.cartoshka.tree.entities;

public class UnaryOperation extends Expression {
    private final String operator;

    private final Expression expression;

    public UnaryOperation(String operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }
}
