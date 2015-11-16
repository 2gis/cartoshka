package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.GenericVisitor;
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
    public NodeType type() {
        return NodeType.ELEMENT;
    }

    @Override
    public <R, P> R accept(GenericVisitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }

    @Override
    public <P> void accept(Visitor<P> visitor, P params) {
        visitor.visit(this, params);
    }

    public enum ElementType {
        MAP,
        ID,
        CLASS,
        WILDCARD
    }
}
