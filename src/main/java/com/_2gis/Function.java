package com._2gis;

import com._2gis.tree.entities.Literal;

import java.util.Iterator;

public interface Function {
    String getName();

    int getArgumentCount();

    Literal apply(Location location, Iterator<Literal> args);
}