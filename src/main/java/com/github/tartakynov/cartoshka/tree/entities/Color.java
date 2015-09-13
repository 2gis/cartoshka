package com.github.tartakynov.cartoshka.tree.entities;

public class Color extends Expression {
    private final int r;
    private final int g;
    private final int b;
    private final int a;

    public Color(int r, int g, int b) {
        this(r, g, b, 0);
    }

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}