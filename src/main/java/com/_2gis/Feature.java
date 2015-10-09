package com._2gis;

import com._2gis.tree.entities.Literal;

import java.util.Set;

public interface Feature {
    String getLayer();

    Set<String> getClasses();

    Literal getField(String fieldName);
}