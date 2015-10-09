package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.scanner.TokenType;

public class BinaryOperation extends Expression {
    private final TokenType operator;

    private Expression left;

    private Expression right;

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

    @Override
    public void fold() {
        left = fold(left);
        right = fold(right);
    }
}