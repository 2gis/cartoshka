package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Color extends Literal {
    private final int r;
    private final int g;
    private final int b;
    private final int a;

    public Color(int r, int g, int b) {
        this(r, g, b, -1);
    }

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public boolean isColor() {
        return true;
    }
}