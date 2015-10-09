package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.Location;
import com.github.tartakynov.cartoshka.tree.entities.Expression;

import java.util.Collection;

public abstract class Node {
    private Location location;

    public Location getLocation() {
        return location;
    }

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

    public abstract void fold();
}