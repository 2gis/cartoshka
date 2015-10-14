package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.*;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.entities.Expression;
import com._2gis.cartoshka.tree.entities.Literal;

public class Filter extends Node implements Evaluable<Boolean> {
    private final TokenType operator;

    private Expression left;

    private Expression right;

    public Filter(Location location, TokenType operator, Expression left, Expression right) {
        super(location);
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

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitFilter(this, params);
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

        throw CartoshkaException.invalidOperation(getLocation());
    }
}