package com._2gis.cartoshka;

public class Location {
    public final String name;

    public final int offset;

    public final int line;

    public final int linePos;

    public Location(String name, int offset, int line, int linePos) {
        this.name = name;
        this.offset = offset;
        this.line = line;
        this.linePos = linePos;
    }

    public static Location min(Location left, Location right) {
        if (left == null || right == null) {
            return null;
        }

        return left.offset < right.offset ? left : right;
    }
}