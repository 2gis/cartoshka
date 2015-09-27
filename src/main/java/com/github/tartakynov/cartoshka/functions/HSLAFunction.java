package com.github.tartakynov.cartoshka.functions;

import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;

public class HSLAFunction extends RGBAFunction {
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
        throw new NotImplementedException();
    }
}