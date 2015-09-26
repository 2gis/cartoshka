package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Dimension extends Literal {
    private final double value;

    private final String unit;

    public Dimension(double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        return null;
    }

    @Override
    public Literal operate(TokenType operator) {
        return new Dimension(-value, unit);
    }

    @Override
    public boolean isDimension() {
        return true;
    }


}
