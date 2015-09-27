package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Numeric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HSLFunction extends HSLAFunction {
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
        List<Expression> extended = new ArrayList<>(args);
        extended.add(new Numeric(1.0, true));
        return super.apply(extended);
    }
}