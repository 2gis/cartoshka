package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.exceptions.OperationException;
import com.github.tartakynov.cartoshka.scanners.TokenType;

public class UnaryOperation extends Expression {
    private final TokenType operator;

    private final Expression expression;

    public UnaryOperation(TokenType operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public Expression ev() {
        Expression operand = expression.ev();
        switch (operator) {
            case SUB:
                if (operand instanceof Literal) {
                    // numeric literal
                    Literal a = (Literal) operand;
                    return new Literal(-a.getValue().doubleValue());
                } else if (operand instanceof Dimension) {
                    // dimension
                    Dimension a = (Dimension) operand;
                    return new Dimension(-a.getValue().doubleValue(), a.getUnit());
                }

                break;
        }

        String operandType = operand.getClass().getSimpleName().toLowerCase();
        throw new OperationException("Operator [-] cannot be applied to " + operandType);
    }
}