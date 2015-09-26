package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
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
        Double r = iterator.next().ev().toNumber();
        Double g = iterator.next().ev().toNumber();
        Double b = iterator.next().ev().toNumber();
        if (r == null) throw new ArgumentException(getName(), "r");
        if (g == null) throw new ArgumentException(getName(), "g");
        if (b == null) throw new ArgumentException(getName(), "b");
        if (Math.floor(r) != r) r *= 0xFF;
        if (Math.floor(g) != g) g *= 0xFF;
        if (Math.floor(b) != b) b *= 0xFF;

        return new Color(r.intValue(), g.intValue(), b.intValue());
    }
}