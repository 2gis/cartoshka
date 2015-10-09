package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.entities.Literal;

import java.util.Iterator;

public interface Function {
    String getName();

    int getArgumentCount();

    Literal apply(Location location, Iterator<Literal> args);
}