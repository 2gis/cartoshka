package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

import java.util.Iterator;

public abstract class BaseFunction implements Function {
    protected final String name;
    protected final int argc;

    protected BaseFunction(String name, int argc) {
        this.name = name;
        this.argc = argc;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public abstract Literal apply(Iterator<Expression> args);

    @Override
    public int getArgumentCount() {
        return argc;
    }
}
