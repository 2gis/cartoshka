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
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitBinaryOperationExpression(this, params);
    }
}