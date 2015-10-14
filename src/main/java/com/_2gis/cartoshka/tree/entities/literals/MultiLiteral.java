package com._2gis.cartoshka.tree.entities.literals;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.tree.entities.Literal;

import java.util.Collection;
import java.util.Iterator;

public class MultiLiteral extends Literal {
    private final Collection<Literal> value;

    public MultiLiteral(Location location, Collection<Literal> literals) {
        super(location);
        this.value = literals;
    }


    @Override
    public NodeType type() {
        return NodeType.MULTI_LITERAL;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitMultiLiteral(this, params);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (builder.length() == 0) {
            Iterator<? extends Literal> iterator = value.iterator();
            while (iterator.hasNext()) {
                builder.append(iterator.next().toString());
                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }
        }

        return builder.toString();
    }

    public Collection<Literal> getValue() {
        return value;
    }

    @Override
    public int compareTo(Literal o) {
        throw CartoshkaException.incorrectComparison(getLocation());
    }
}