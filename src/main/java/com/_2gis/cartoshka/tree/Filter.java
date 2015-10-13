package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
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

    public Expression getRight() {
        return right;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitFilter(this);
    }

    @Override
    public String toString() {
        return String.format("[%s %s %s]", left.toString(), operator.getStr(), right.toString());
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

        throw CartoshkaException.invalidOperation(getLocation());
    }
}