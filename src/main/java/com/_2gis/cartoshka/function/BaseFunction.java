package com._2gis.cartoshka.function;

import com._2gis.cartoshka.Function;

public abstract class BaseFunction implements Function {
    private final String name;
    private final int argc;

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
