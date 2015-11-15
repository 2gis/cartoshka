package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.expression.Literal;

import java.util.Set;

public interface Feature {
    String getLayer();

    Set<String> getClasses();

    Literal getField(String fieldName);
}