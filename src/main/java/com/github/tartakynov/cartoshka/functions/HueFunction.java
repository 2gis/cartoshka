package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;
import com.github.tartakynov.cartoshka.tree.entities.literals.Numeric;

import java.util.Collection;

public class HueFunction extends HSLAFunction {
    @Override
    public String getName() {
        return "hue";
    }

    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public Literal apply(Collection<Expression> args) {
        Literal arg = args.iterator().next().ev();
        if (arg != null && arg.isColor()) {
            Color color = (Color) arg;
            return new Numeric(color.getHue(), false);
        }

        return null;
    }
}

