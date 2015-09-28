package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Color;

import java.util.Collection;
import java.util.Iterator;

public class HSLFunction implements Function {
    @Override
    public String getName() {
        return "hsl";
    }

    @Override
    public int getArgumentsCount() {
        return 3;
    }

    @Override
    public Literal apply(Collection<Expression> args) {
        Iterator<Expression> iterator = args.iterator();
        Double h = Arguments.numeric(iterator.next().ev(), getName(), "h", false);
        Double s = Arguments.numeric(iterator.next().ev(), getName(), "s", false);
        Double l = Arguments.numeric(iterator.next().ev(), getName(), "l", false);
        return Color.fromHSLA(h.intValue(), s, l, 1.0);
    }
}