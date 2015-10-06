package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.tree.entities.literals.MultiLiteral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Value extends Expression {
    private final Collection<Expression> expressions;

    public Value(Collection<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Literal ev(Feature feature) {
        if (expressions.size() == 1) {
            return expressions.iterator().next().ev(feature);
        }

        List<Literal> literals = new ArrayList<>();
        for (Expression expression : expressions) {
            literals.add(expression.ev(feature));
        }

        return new MultiLiteral(literals);
    }

    @Override
    public boolean isDynamic() {
        return hasDynamicExpression(expressions);
    }
}