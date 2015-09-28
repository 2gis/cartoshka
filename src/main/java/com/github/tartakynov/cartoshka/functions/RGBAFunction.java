package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;

import java.util.Collection;
import java.util.Iterator;

public class RGBAFunction implements Function {
    @Override
    public String getName() {
        return "rgba";
    }

    @Override
    public int getArgumentsCount() {
        return 4;
    }

    @Override
    public Literal apply(Collection<Expression> args) {
        Iterator<Expression> iterator = args.iterator();
        Double r = Arguments.numeric(iterator.next().ev(), getName(), "r", true);
        Double g = Arguments.numeric(iterator.next().ev(), getName(), "g", true);
        Double b = Arguments.numeric(iterator.next().ev(), getName(), "b", true);
        Double a = Arguments.numeric(iterator.next().ev(), getName(), "a", false);
        return Color.fromRGBA(r.intValue(), g.intValue(), b.intValue(), a);
    }
}