package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;
import com.github.tartakynov.cartoshka.tree.entities.literals.Dimension;
import com.github.tartakynov.cartoshka.tree.entities.literals.Numeric;

import java.util.Iterator;

public class Functions {
    public static Function hsla = new BaseFunction("hsla") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Double h = Arguments.numeric(args, "h", getName(), false);
            Double s = Arguments.numeric(args, "s", getName(), false);
            Double l = Arguments.numeric(args, "l", getName(), false);
            Double a = Arguments.numeric(args, "a", getName(), false);
            return Color.fromHSLA(h.intValue(), s, l, a);
        }
    };

    public static Function hsl = new BaseFunction("hsl") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Double h = Arguments.numeric(args, "h", getName(), false);
            Double s = Arguments.numeric(args, "s", getName(), false);
            Double l = Arguments.numeric(args, "l", getName(), false);
            return Color.fromHSLA(h.intValue(), s, l, 1.0);
        }
    };

    public static Function rgba = new BaseFunction("rgba") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Double r = Arguments.numeric(args, "r", getName(), true);
            Double g = Arguments.numeric(args, "g", getName(), true);
            Double b = Arguments.numeric(args, "b", getName(), true);
            Double a = Arguments.numeric(args, "a", getName(), false);
            return Color.fromRGBA(r.intValue(), g.intValue(), b.intValue(), a);
        }
    };

    public static Function rgb = new BaseFunction("rgb") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Double r = Arguments.numeric(args, "r", getName(), true);
            Double g = Arguments.numeric(args, "g", getName(), true);
            Double b = Arguments.numeric(args, "b", getName(), true);
            return Color.fromRGBA(r.intValue(), g.intValue(), b.intValue(), 1.0);
        }
    };

    public static Function hue = new BaseFunction("hue") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            return new Numeric(color.getHue(), false);
        }
    };

    public static Function saturation = new BaseFunction("saturation") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            return new Dimension(color.getSaturation() * 100, "%");
        }
    };

    public static Function lightness = new BaseFunction("lightness") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            return new Dimension(color.getLightness() * 100, "%");
        }
    };

    public static Function alpha = new BaseFunction("alpha") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            return new Dimension(color.getAlpha() * 100, "%");
        }
    };

    public static Function saturate = new BaseFunction("saturate") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            Double amount = Arguments.numeric(args, "amount", getName(), false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation() + amount,
                    color.getLightness(),
                    color.getAlpha()
            );
        }
    };

    public static Function desaturate = new BaseFunction("desaturate") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            Double amount = Arguments.numeric(args, "amount", getName(), false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation() - amount,
                    color.getLightness(),
                    color.getAlpha()
            );
        }
    };

    public static Function lighten = new BaseFunction("lighten") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            Double amount = Arguments.numeric(args, "amount", getName(), false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation(),
                    color.getLightness() + amount,
                    color.getAlpha()
            );
        }
    };

    public static Function darken = new BaseFunction("darken") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            Double amount = Arguments.numeric(args, "amount", getName(), false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation(),
                    color.getLightness() - amount,
                    color.getAlpha()
            );
        }
    };

    public static Function fadein = new BaseFunction("fadein") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            Double amount = Arguments.numeric(args, "amount", getName(), false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation(),
                    color.getLightness(),
                    color.getAlpha() + amount
            );
        }
    };

    public static Function fadeout = new BaseFunction("fadeout") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            Double amount = Arguments.numeric(args, "amount", getName(), false);
            return Color.fromHSLA(
                    color.getHue(),
                    color.getSaturation(),
                    color.getLightness(),
                    color.getAlpha() - amount
            );
        }
    };

    public static Function spin = new BaseFunction("spin") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            int amount = Arguments.numeric(args, "amount", getName(), false).intValue();
            int hue = (color.getHue() + amount) % 360;
            return Color.fromHSLA(
                    hue < 0 ? 360 + hue : hue,
                    color.getSaturation(),
                    color.getLightness(),
                    color.getAlpha()
            );
        }
    };

    public static Function greyscale = new BaseFunction("greyscale") {
        @Override
        public Literal apply(Iterator<Expression> args) {
            Color color = Arguments.color(args, "color", getName());
            return Color.fromHSLA(
                    color.getHue(),
                    0,
                    color.getLightness(),
                    color.getAlpha()
            );
        }
    };
}