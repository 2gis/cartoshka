package com.github.tartakynov.cartoshka.tree;

public class Element extends Node {
    private final String value;
    private final ElementType type;

    public Element(String value, ElementType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public void fold() {
    }

    public enum ElementType {
        MAP,
        ID,
        CLASS,
        WILDCARD
    }
}
