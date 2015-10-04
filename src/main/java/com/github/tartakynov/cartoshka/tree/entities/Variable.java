package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.VariableContext;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.tree.Rule;
import com.github.tartakynov.cartoshka.tree.entities.literals.MultiLiteral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Variable extends Expression {
    private final String name;
    private final VariableContext context;

    public Variable(VariableContext context, String name) {
        this.context = context;
        this.name = name;
    }

    @Override
    public Literal ev() {
        Rule variable = context.get(name);
        if (variable == null) {
            throw new CartoshkaException(String.format("Undefined variable: %s", name));
        }

        Collection<Expression> expressions = variable.getValue().getExpressions();
        if (expressions.size() == 1) {
            return expressions.iterator().next().ev();
        }

        List<Literal> literals = new ArrayList<>();
        for (Expression expression : expressions) {
            literals.add(expression.ev());
        }

        return new MultiLiteral(literals);
    }
}