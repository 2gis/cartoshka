package com.github.tartakynov.cartoshka.tree.entities;

public class BinaryOperation extends Expression {
    private final String operator;

    private final Expression left;

    private final Expression right;

    public BinaryOperation(String operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
}
