package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Color extends Literal {
    private final int r;
    private final int g;
    private final int b;
    private final double a;

    public Color(int r, int g, int b, double a) {
        this.r = Math.max(0, Math.min(r, 0xFF));
        this.g = Math.max(0, Math.min(g, 0xFF));
        this.b = Math.max(0, Math.min(b, 0xFF));
        this.a = Math.max(0.0, Math.min(a, 1.0));
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 1.0);
    }

    @Override
    public boolean isColor() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("rgba(%d, %d, %d, %s)", r, g, b, Double.toString(a));
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        Color right = null;
        if (operand.isColor()) {
            right = (Color) operand;
        } else if (operand.isDimension() && operand.toNumber() != null) {
            int v = (int) (operand.toNumber() * 0xFF);
            right = new Color(v, v, v);
        }

        if (right != null) {
            switch (operator) {
                case ADD:
                    return new Color(r + right.r, g + right.g, b + right.b);
                case SUB:
                    return new Color(r - right.r, g - right.g, b - right.b);
                case MUL:
                    return new Color(r * right.r, g * right.g, b * right.b);
                case DIV:
                    return new Color(r / right.r, g / right.g, b / right.b);
            }
        }

        return super.operate(operator, operand);
    }
}