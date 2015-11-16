package com._2gis.cartoshka.tree.expression.literal;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.tree.expression.Literal;
import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Visitor;

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
    public <R, P> R accept(GenericVisitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
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

    @Override
    public <P> void accept(Visitor<P> visitor, P params) {
        visitor.visit(this, params);
    }
}