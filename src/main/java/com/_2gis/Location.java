package com._2gis;

public class Location {
    public final int offset;

    public final int line;

    public final int linePos;

    public Location(int offset, int line, int linePos) {
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