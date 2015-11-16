package com._2gis.cartoshka.tree.expression.literal;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.tree.expression.Literal;
import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Visitor;

public class ImageFilter extends Literal {
    private final String name;

    private final MultiLiteral args;

    public ImageFilter(Location location, String name, MultiLiteral args) {
        super(location);
        this.name = name;
        this.args = args;
    }

    @Override
    public NodeType type() {
        return NodeType.IMAGE_FILTER;
    }

    @Override
    public <R, P> R accept(GenericVisitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }

    @Override
    public <P> void accept(Visitor<P> visitor, P params) {
        visitor.visit(this, params);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, args.toString());
    }

    public String getName() {
        return name;
    }

    public MultiLiteral getArgs() {
        return args;
    }

    @Override
    public int compareTo(Literal o) {
        throw CartoshkaException.incorrectComparison(getLocation());
    }
}