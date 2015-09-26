package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

import java.util.Collection;

public interface Function {
    String getName();

    int getArgumentsCount();

    Literal apply(Collection<Expression> args);
}
