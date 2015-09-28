package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Color extends Literal {
    private final int red;
    private final int green;
    private final int blue;
    private final double alpha;
    private final int hue;
    private final double saturation;
    private final double lightness;

    private Color(int r, int g, int b, int h, double s, double l, double a) {
        this.alpha = Math.max(0.0, Math.min(a, 1.0));
        this.red = Math.max(0, Math.min(r, 0xFF));
        this.green = Math.max(0, Math.min(g, 0xFF));
        this.blue = Math.max(0, Math.min(b, 0xFF));
        this.hue = h;
        this.saturation = Math.round(s * 100) / 100.0;
        this.lightness = Math.round(l * 100) / 100.0;
    }

    protected static double hue(double h, double m2, double m1) {
        h = h < 0 ? h + 1.0 : (h > 1.0 ? h - 1.0 : h);
        if (h * 6.0 < 1) {
            return m1 + (m2 - m1) * h * 6.0;
        } else if (h * 2.0 < 1.0) {
            return m2;
        } else if (h * 3.0 < 2.0) {
            return m1 + (m2 - m1) * (2.0 / 3.0 - h) * 6.0;
        }

        return m1;
    }

    public static Color fromRGBA(int r, int g, int b, double a) {
        double _r = r / 255.0;
        double _g = g / 255.0;
        double _b = b / 255.0;
        double max = Math.max(_r, Math.max(_g, _b));
        double min = Math.min(_r, Math.min(_g, _b));
        double h;
        double s;
        double l = (max + min) / 2;
        double d = max - min;
        if (max == min) {
            h = s = 0;
        } else {
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
            if (max == _r) {
                h = (_g - _b) / d + (_g < _b ? 6.0 : 0.0);
            } else if (max == _g) {
                h = (_b - _r) / d + 2.0;
            } else {
                h = (_r - _g) / d + 4;
            }

            h /= 6;
        }

        h = h * 360;
        return new Color(r, g, b, (int) Math.round(h), s, l, a);
    }

    public static Color fromHSLA(int h, double s, double l, double a) {
        double _h = (h % 360) / 360.0;
        double m2 = l <= 0.5 ? l * (s + 1.0) : l + s - l * s;
        double m1 = l * 2.0 - m2;
        int r = (int) Math.round(hue(_h + 1.0 / 3, m2, m1) * 0xFF);
        int g = (int) Math.round(hue(_h, m2, m1) * 255);
        int b = (int) Math.round(hue(_h - 1.0 / 3, m2, m1) * 0xFF);
        return new Color(r, g, b, h, s, l, a);
    }

    public int getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getLightness() {
        return lightness;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public double getAlpha() {
        return alpha;
    }

    @Override
    public boolean isColor() {
        return true;
    }

    @Override
    public String toString() {
        if (alpha == 1.0) {
            return String.format("rgb(%d, %d, %d)", red, green, blue);
        }

        return String.format("rgba(%d, %d, %d, %s)", red, green, blue, Double.toString(alpha));
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        Color right = null;
        if (operand.isColor()) {
            right = (Color) operand;
        } else if (operand.isDimension() && operand.toNumber() != null) {
            int v = (int) (operand.toNumber() * 0xFF);
            right = fromRGBA(v, v, v, 1.0);
        }

        if (right != null) {
            switch (operator) {
                case ADD:
                    return Color.fromRGBA(red + right.red, green + right.green, blue + right.blue, alpha);
                case SUB:
                    return Color.fromRGBA(red - right.red, green - right.green, blue - right.blue, alpha);
                case MUL:
                    return Color.fromRGBA(red * right.red, green * right.green, blue * right.blue, alpha);
                case DIV:
                    return Color.fromRGBA(red / right.red, green / right.green, blue / right.blue, alpha);
            }
        }

        return super.operate(operator, operand);
    }
}