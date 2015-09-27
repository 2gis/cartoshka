package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Numeric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class HSLAFunction extends RGBAFunction {
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

    @Override
    public String getName() {
        return "hsla";
    }

    @Override
    public int getArgumentsCount() {
        return 4;
    }

    @Override
    public Literal apply(Collection<Expression> args) {
        Iterator<Expression> iterator = args.iterator();
        double h = (literalToArgument(iterator.next().ev(), "h", false) % 360) / 360.0;
        double s = literalToArgument(iterator.next().ev(), "s", false);
        double l = literalToArgument(iterator.next().ev(), "l", false);
        double a = literalToArgument(iterator.next().ev(), "a", false);
        double m2 = l <= 0.5 ? l * (s + 1.0) : l + s - l * s;
        double m1 = l * 2.0 - m2;

        List<Expression> arguments = new ArrayList<>();
        arguments.add(new Numeric(Math.round(hue(h + 1.0 / 3, m2, m1) * 0xFF), false));
        arguments.add(new Numeric(Math.round(hue(h, m2, m1) * 255), false));
        arguments.add(new Numeric(Math.round(hue(h - 1.0 / 3, m2, m1) * 0xFF), false));
        arguments.add(new Numeric(a, true));
        return super.apply(arguments);
    }
}