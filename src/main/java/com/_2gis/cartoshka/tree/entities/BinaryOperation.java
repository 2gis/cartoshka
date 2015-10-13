package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.scanner.TokenType;

public class BinaryOperation extends Expression {
    private final TokenType operator;

    private Expression left;

    private Expression right;

    public BinaryOperation(Location location, TokenType operator, Expression left, Expression right) {
        super(location);
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
    public void accept(Visitor visitor) {
        visitor.visitBinaryOperation(this);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", left.toString(), operator.getStr(), right.toString());
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