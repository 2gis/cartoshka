package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.tree.entities.Literal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MultiLiteral extends Literal {
    private final Collection<Literal> value;

    private final StringBuilder builder = new StringBuilder();

    public MultiLiteral(Collection<Literal> literals) {
        this.value = literals;
    }

    public MultiLiteral(Literal... literals) {
        this.value = Arrays.asList(literals);
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
}