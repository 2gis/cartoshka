package com.github.tartakynov.cartoshka.scanner;

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
}