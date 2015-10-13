package com._2gis.cartoshka.tree.entities.literals;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.entities.Literal;

import java.util.Collection;

public class MultiLiteral extends Literal {
    private final Collection<Literal> value;

    public MultiLiteral(Location location, Collection<Literal> literals) {
        super(location);
        this.value = literals;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitMultiLiteral(this);
    }

    @Override
    public String toString() {
        return String.format("[%s]", collectionToString(value, ", "));
    }

    public Collection<Literal> getValue() {
        return value;
    }

    @Override
    public boolean isMulti() {
        return true;
    }

    @Override
    public int compareTo(Literal o) {
        throw CartoshkaException.incorrectComparison(getLocation());
    }
}