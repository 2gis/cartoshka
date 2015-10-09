package com._2gis.tree;

import com._2gis.CartoshkaException;
import com._2gis.Feature;
import com._2gis.Location;
import com._2gis.scanner.TokenType;
import com._2gis.tree.entities.Expression;
import com._2gis.tree.entities.Literal;

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

    @Override
    public void fold() {
        expression = Node.fold(expression);
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