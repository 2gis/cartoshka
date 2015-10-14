package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.*;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.entities.Expression;
import com._2gis.cartoshka.tree.entities.Literal;

public class Zoom extends Node implements Evaluable<Double> {
    private final TokenType operator;
    private Expression expression;

    public Zoom(Location location, TokenType operator, Expression expression) {
        super(location);
        this.operator = operator;
        this.expression = expression;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitZoom(this, params);
    }

    @Override
    public Double ev(Feature feature) {
        Literal literal = expression.ev(feature);
        if (literal.isNumeric()) {
            return literal.toNumber();
        }

        throw CartoshkaException.invalidOperation(getLocation());
    }
}