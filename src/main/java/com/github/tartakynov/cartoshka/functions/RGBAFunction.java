package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
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
        Double r = literalToArgument(iterator.next().ev(), "r", true);
        Double g = literalToArgument(iterator.next().ev(), "g", true);
        Double b = literalToArgument(iterator.next().ev(), "b", true);
        Double a = literalToArgument(iterator.next().ev(), "a", false);

        return new Color(r.intValue(), g.intValue(), b.intValue(), a);
    }

    protected Double literalToArgument(Literal literal, String name, boolean mapToFF) {
        Double arg = literal.toNumber();
        if (arg == null) {
            throw new ArgumentException(getName(), name);
        }

        return (literal.hasDot() && mapToFF) ? arg * 0xFF : arg;
    }
}