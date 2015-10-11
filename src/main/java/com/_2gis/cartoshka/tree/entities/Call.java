package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Function;
import com._2gis.cartoshka.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Call extends Expression {
    private final Function function;
    private Collection<Expression> args;

    public Call(Location location, Function function, Collection<Expression> args) {
        super(location);
        this.function = function;
        this.args = args;
    }

    @Override
    public Literal ev(final Feature feature) {
        return function.apply(getLocation(), new Iterator<Literal>() {
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

    @Override
    public String toString() {
        return String.format("%s(%s)", function.getName(), collectionToString(args, ", "));
    }
}