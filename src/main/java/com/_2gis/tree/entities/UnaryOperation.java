package com._2gis.tree.entities;

import com._2gis.Feature;
import com._2gis.Location;
import com._2gis.scanner.TokenType;

public class UnaryOperation extends Expression {
    private final TokenType operator;

    private Expression expression;

    public UnaryOperation(Location location, TokenType operator, Expression expression) {
        super(location);
        this.operator = operator;
        this.expression = expression;
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