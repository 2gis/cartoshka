package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.Location;

public class Element extends Node implements Evaluable<Boolean> {
    private final String value;
    private final ElementType type;

    public Element(Location location, String value, ElementType type) {
        super(location);
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public ElementType getType() {
        return type;
    }

    public Boolean ev(Feature feature) {
        switch (getType()) {
            case CLASS:
                if (feature.getClasses().contains(value)) {
                    return true;
                }

                break;

            case ID:
                if (value.equals(feature.getLayer())) {
                    return true;
                }

                break;

            case WILDCARD:
                return true;
        }

        return false;
    }

    @Override
    public void fold() {
    }

    public enum ElementType {
        MAP,
        ID,
        CLASS,
        WILDCARD
    }
}
