package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.scanner.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Filter extends Node implements Evaluable<Boolean> {
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

    @Override
    public Boolean ev(Feature feature) {
        Literal lh = left.ev(feature);
        Literal rh = right.ev(feature);
        switch (operator) {
            case EQ:
                return lh.compareTo(rh) == 0;

            case NE:
                return lh.compareTo(rh) != 0;

            case LT:
                return lh.compareTo(rh) < 0;

            case GT:
                return lh.compareTo(rh) > 0;

            case LTE:
                return lh.compareTo(rh) <= 0;

            case GTE:
                return lh.compareTo(rh) >= 0;
        }

        throw CartoshkaException.invalidOperation(this);
    }
}