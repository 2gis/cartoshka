package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.Location;
import com.github.tartakynov.cartoshka.scanner.TokenType;

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