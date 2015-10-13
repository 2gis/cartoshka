package com._2gis.cartoshka.tree.entities.literals;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.entities.Literal;

public class ImageFilter extends Literal {
    private final String name;

    private final MultiLiteral args;

    public ImageFilter(Location location, String name, MultiLiteral args) {
        super(location);
        this.name = name;
        this.args = args;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitImageFilter(this);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, args.toString());
    }

    @Override
    public boolean isImageFilter() {
        return true;
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