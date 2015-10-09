package com.github.tartakynov.cartoshka;

public class Location {
    public final int offset;

    public final int line;

    public final int linePos;

    public final int size;

    public Location(int offset, int line, int linePos, int size) {
        this.offset = offset;
        this.line = line;
        this.linePos = linePos;
        this.size = size;
    }

    public static Location combine(Location left, Location right) {
        if (left == null || right == null) {
            return null;
        }

        Location min = left.offset < right.offset ? left : right;
        Location max = left.offset > right.offset ? left : right;
        int offset = min.offset;
        int line = min.line;
        int linePos = min.linePos;
        int size = (max.offset + max.size) - min.offset;
        return new Location(offset, line, linePos, size);
    }
}