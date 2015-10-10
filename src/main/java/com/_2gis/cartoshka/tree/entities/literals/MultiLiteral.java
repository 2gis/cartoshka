package com._2gis.cartoshka.tree.entities.literals;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.tree.entities.Literal;

import java.util.Collection;
import java.util.Iterator;

public class MultiLiteral extends Literal {
    private final Collection<Literal> value;

    private final StringBuilder builder = new StringBuilder();

    public MultiLiteral(Location location, Collection<Literal> literals) {
        super(location);
        this.value = literals;
    }

    @Override
    public String toString() {
        if (builder.length() == 0) {
            builder.append('[');
            Iterator<Literal> iterator = value.iterator();
            while (iterator.hasNext()) {
                builder.append(iterator.next().toString());
                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }

            builder.append(']');
        }

        return builder.toString();
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