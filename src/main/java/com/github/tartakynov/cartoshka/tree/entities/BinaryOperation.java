package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
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
    public Literal ev(Feature feature) {
        Literal leftOp = left.ev(feature);
        Literal rightOp = right.ev(feature);
        if ((!leftOp.isColor() && rightOp.isColor()) || (leftOp.isNumeric() && rightOp.isDimension())) {
            Literal tmp = leftOp;
            leftOp = rightOp;
            rightOp = tmp;
        }

        return leftOp.operate(operator, rightOp);
    }

    @Override
    public boolean isDynamic() {
        return left.isDynamic() || right.isDynamic();
    }
}