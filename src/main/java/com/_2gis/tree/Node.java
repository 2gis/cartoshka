package com._2gis.tree;

import com._2gis.Location;
import com._2gis.tree.entities.Expression;

import java.util.Collection;

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

    public Location getLocation() {
        return location;
    }

    public abstract void fold();
}