package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Expression;

public class Filter extends Node {
    private final TokenType operator;

    private Expression left;

    private Expression right;

    public Filter(TokenType operator, Expression left, Expression right) {
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

    @Override
    public void fold() {
        left = fold(left);
        right = fold(right);
    }
}