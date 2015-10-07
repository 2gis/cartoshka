package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Call extends Expression {
    private final Function function;
    private Collection<Expression> args;

    public Call(Function function, Collection<Expression> args) {
        this.function = function;
        this.args = args;
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
        return hasDynamicExpression(args);
    }

    @Override
    public void fold() {
        Collection<Expression> newArgs = new ArrayList<>();
        for (Expression expression : args) {
            newArgs.add(fold(expression));
        }

        args = newArgs;
    }
}