package com.github.tartakynov.cartoshka.scanner;

import java.io.Reader;

public final class Source {
    public final String name;

    public final Reader reader;

    public Source(String name, Reader reader) {
        this.name = name;
        this.reader = reader;
    }
}
