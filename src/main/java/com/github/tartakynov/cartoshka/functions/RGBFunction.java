package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;

import java.util.Collection;
import java.util.Iterator;

public class RGBFunction implements Function {
    @Override
    public String getName() {
        return "rgb";
    }

    @Override
    public int getArgumentsCount() {
        return 3;
    }

    @Override
    public Literal apply(Collection<Expression> args) {
        Iterator<Expression> iterator = args.iterator();
        Double r = Arguments.numeric(iterator.next().ev(), getName(), "r", true);
        Double g = Arguments.numeric(iterator.next().ev(), getName(), "g", true);
        Double b = Arguments.numeric(iterator.next().ev(), getName(), "b", true);
        return Color.fromRGBA(r.intValue(), g.intValue(), b.intValue(), 1.0);
    }
}