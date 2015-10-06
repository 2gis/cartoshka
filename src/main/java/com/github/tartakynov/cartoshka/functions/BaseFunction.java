package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;

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
    public int getArgumentCount() {
        return argc;
    }
}
