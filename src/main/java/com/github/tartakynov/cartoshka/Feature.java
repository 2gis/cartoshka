package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.entities.Literal;

import java.util.Set;

public interface Feature {
    String getLayer();

    Set<String> getClasses();

    Literal getField(String fieldName);
}