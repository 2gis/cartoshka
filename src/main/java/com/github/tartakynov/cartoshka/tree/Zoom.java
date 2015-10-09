package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.scanner.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Zoom extends Node implements Evaluable<Double> {
    private final TokenType operator;
    private Expression expression;

    public Zoom(TokenType operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public TokenType getOperator() {
        return operator;
    }

    @Override
    public void fold() {
        expression = fold(expression);
    }

    @Override
    public Double ev(Feature feature) {
        Literal literal = expression.ev(feature);
        if (literal.isNumeric()) {
            return literal.toNumber();
        }

        throw CartoshkaException.invalidOperation(this);
    }
}