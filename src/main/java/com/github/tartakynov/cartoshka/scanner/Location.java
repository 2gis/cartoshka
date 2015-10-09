package com.github.tartakynov.cartoshka.scanner;

public class Location {
    public final int offset;

    public final int line;

    public final int linePos;

    public Location(int offset, int line, int linePos) {
        this.offset = offset;
        this.line = line;
        this.linePos = linePos;
    }
}