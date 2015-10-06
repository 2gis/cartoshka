package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.Function;

import java.util.Collection;
import java.util.Iterator;

public class Call extends Expression {
    private final Function function;
    private final Collection<Expression> args;
    private final boolean isDynamic;

    public Call(Function function, Collection<Expression> args) {
        this.function = function;
        this.args = args;
        this.isDynamic = hasDynamicExpression(args);
    }

    @Override
    public Literal ev(final Feature feature) {
        return function.apply(new Iterator<Literal>() {
            Iterator<Expression> iterator = args.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Literal next() {
                return iterator.next().ev(feature);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Override
    public boolean isDynamic() {
        return isDynamic;
    }
}