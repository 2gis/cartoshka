package com._2gis.cartoshka.tree.entities.literals;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.tree.entities.Literal;

public class Boolean extends Literal {
    private final boolean value;

    public Boolean(Location location, boolean value) {
        super(location);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public NodeType type() {
        return NodeType.BOOLEAN;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitBooleanLiteral(this, params);
    }

    @Override
    public String toString() {
        return this.value ? "true" : "false";
    }

    @Override
    public int compareTo(Literal o) {
        if (o.type() == NodeType.BOOLEAN) {
            Boolean other = (Boolean) o;
            return java.lang.Boolean.compare(getValue(), other.getValue());
        }

        throw CartoshkaException.incorrectComparison(getLocation());
    }
}