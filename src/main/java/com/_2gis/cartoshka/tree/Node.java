package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

public abstract class Node {
    private final Location location;

    public Node(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public abstract <R, P> R accept(Visitor<R, P> visitor, P params);
}