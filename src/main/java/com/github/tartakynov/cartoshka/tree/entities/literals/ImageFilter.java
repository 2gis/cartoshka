package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.Location;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class ImageFilter extends Literal {
    private final String name;

    private final MultiLiteral args;

    public ImageFilter(Location location, String name, MultiLiteral args) {
        super(location);
        this.name = name;
        this.args = args;
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