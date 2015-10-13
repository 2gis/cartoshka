package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

import java.util.Iterator;

public abstract class Node {
    private Location location;

    public Node(Location location) {
        this.location = location;
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

    public abstract <R, P> R accept(Visitor<R, P> visitor, P params);

    public Location getLocation() {
        return location;
    }

    @Override
    public abstract String toString();
}