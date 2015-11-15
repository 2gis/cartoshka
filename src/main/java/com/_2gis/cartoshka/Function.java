package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.expression.Literal;

import java.util.Iterator;

public interface Function {
    String getName();

    int getArgumentCount();

    Literal apply(Location location, Iterator<Literal> args);
}