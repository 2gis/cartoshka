package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

public class Element extends Node {
    private final String value;
    private final ElementType type;

    public Element(Location location, String value, ElementType type) {
        super(location);
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public ElementType getType() {
        return type;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitElement(this, params);
    }

    public enum ElementType {
        MAP,
        ID,
        CLASS,
        WILDCARD
    }
}
