package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.scanner.TokenType;

public class UnaryOperation extends Expression {
    private final TokenType operator;

    private Expression expression;

    public UnaryOperation(Location location, TokenType operator, Expression expression) {
        super(location);
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitUnaryOperation(this, params);
    }

    @Override
    public String toString() {
        return String.format("%s%s", operator.getStr(), expression.toString());
    }

    @Override
    public Literal ev(Feature feature) {
        return expression.ev(feature).operate(operator);
    }

    @Override
    public boolean isDynamic() {
        return expression.isDynamic();
    }

    @Override
    public void fold() {
        expression = fold(expression);
    }
}