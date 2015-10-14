package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

public abstract class Node {
    private Location location;

    public Node(Location location) {
        this.location = location;
    }

    public abstract <R, P> R accept(Visitor<R, P> visitor, P params);

    public Location getLocation() {
        return location;
    }
}