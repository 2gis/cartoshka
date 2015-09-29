package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;
import com.github.tartakynov.cartoshka.tree.entities.literals.Dimension;
import com.github.tartakynov.cartoshka.tree.entities.literals.Numeric;

import java.util.Iterator;

public class Functions {
    public static Function hsla = new Function("hsla") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Double h = argNumeric(args, "h", false);
            Double s = argNumeric(args, "s", false);
            Double l = argNumeric(args, "l", false);
            Double a = argNumeric(args, "a", false);
            return Color.fromHSLA(h.intValue(), s, l, a);
        }
    };

    public static Function hsl = new Function("hsl") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Double h = argNumeric(args, "h", false);
            Double s = argNumeric(args, "s", false);
            Double l = argNumeric(args, "l", false);
            return Color.fromHSLA(h.intValue(), s, l, 1.0);
        }
    };

    public static Function rgba = new Function("rgba") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Double r = argNumeric(args, "r", true);
            Double g = argNumeric(args, "g", true);
            Double b = argNumeric(args, "b", true);
            Double a = argNumeric(args, "a", false);
            return Color.fromRGBA(r.intValue(), g.intValue(), b.intValue(), a);
        }
    };

    public static Function rgb = new Function("rgb") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Double r = argNumeric(args, "r", true);
            Double g = argNumeric(args, "g", true);
            Double b = argNumeric(args, "b", true);
            return Color.fromRGBA(r.intValue(), g.intValue(), b.intValue(), 1.0);
        }
    };

    public static Function hue = new Function("hue") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = argColor(args, "color");
            return new Numeric(color.getHue(), false);
        }
    };

    public static Function saturation = new Function("saturation") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = argColor(args, "color");
            return new Dimension(color.getSaturation() * 100, "%");
        }
    };

    public static Function lightness = new Function("lightness") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = argColor(args, "color");
            return new Dimension(color.getLightness() * 100, "%");
        }
    };

    public static Function alpha = new Function("alpha") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = argColor(args, "color");
            return new Dimension(color.getAlpha() * 100, "%");
        }
    };

    public static Function saturate = new Function("saturate") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = argColor(args, "color");
            Double amount = argNumeric(args, "amount", false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation() + amount,
                    color.getLightness(),
                    color.getAlpha()
            );
        }
    };

    public static Function desaturate = new Function("desaturate") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = argColor(args, "color");
            Double amount = argNumeric(args, "amount", false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation() - amount,
                    color.getLightness(),
                    color.getAlpha()
            );
        }
    };

    public static Function lighten = new Function("lighten") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = argColor(args, "color");
            Double amount = argNumeric(args, "amount", false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation(),
                    color.getLightness() + amount,
                    color.getAlpha()
            );
        }
    };

    public static Function darken = new Function("darken") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = argColor(args, "color");
            Double amount = argNumeric(args, "amount", false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation(),
                    color.getLightness() - amount,
                    color.getAlpha()
            );
        }
    };
}