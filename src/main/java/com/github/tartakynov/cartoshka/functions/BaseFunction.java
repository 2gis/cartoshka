package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

import java.util.Iterator;

public class BaseFunction implements Function {
    protected final String name;

    protected BaseFunction(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Literal apply(Iterator<Expression> args) {
        return null;
    }
}
