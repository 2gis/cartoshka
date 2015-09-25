package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UnaryOperation extends Expression {
    private final TokenType operator;

    private final Expression expression;

    public UnaryOperation(TokenType operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public Expression ev() {
        throw new NotImplementedException();
    }
}
