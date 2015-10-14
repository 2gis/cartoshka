package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Function;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

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

    public Function getFunction() {
        return function;
    }

    public Collection<Expression> getArgs() {
        return args;
    }

    public void setArgs(Collection<Expression> args) {
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
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitCallExpression(this, params);
    }
}