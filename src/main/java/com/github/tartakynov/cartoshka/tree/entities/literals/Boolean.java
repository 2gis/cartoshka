package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.Location;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Boolean extends Literal {
    private final boolean value;

    public Boolean(Location location, boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public String toString() {
        return this.value ? "true" : "false";
    }

    @Override
    public int compareTo(Literal o) {
        if (o.isBoolean()) {
            Boolean other = (Boolean) o;
            return java.lang.Boolean.compare(getValue(), other.getValue());
        }

        throw CartoshkaException.incorrectComparison(this);
    }
}