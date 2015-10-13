package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.tree.entities.Expression;

import java.util.Collection;
import java.util.Iterator;

public abstract class Node {
    private Location location;

    public Node(Location location) {
        this.location = location;
    }

    protected static Expression fold(Expression expression) {
        if (!expression.isDynamic()) {
            return expression.ev(null);
        }

        expression.fold();
        return expression;
    }

    protected static void fold(Collection<? extends Node> nodes) {
        for (Node node : nodes) {
            node.fold();
        }
    }

    protected static String collectionToString(Iterable<? extends Node> args, String delim) {
        StringBuilder builder = new StringBuilder();
        if (builder.length() == 0) {
            Iterator<? extends Node> iterator = args.iterator();
            while (iterator.hasNext()) {
                builder.append(iterator.next().toString());
                if (iterator.hasNext()) {
                    builder.append(delim);
                }
            }
        }

        return builder.toString();
    }

    public abstract void accept(Visitor visitor);

    public Location getLocation() {
        return location;
    }

    @Override
    public abstract String toString();

    public abstract void fold();
}