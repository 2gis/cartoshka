package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Function;
import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
import com.github.tartakynov.cartoshka.exceptions.OperationException;
import com.github.tartakynov.cartoshka.functions.HSLAFunction;
import com.github.tartakynov.cartoshka.functions.RGBAFunction;
import com.github.tartakynov.cartoshka.functions.RGBFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Call extends Expression {
    private final static Map<String, Function> BUILTIN_FUNCTIONS = new HashMap<String, Function>() {{
        put("rgb", new RGBFunction());
        put("rgba", new RGBAFunction());
        put("hsla", new HSLAFunction());
    }};

    private final String function;
    private final Collection<Expression> args;

    public Call(String function, Collection<Expression> args) {
        this.function = function;
        this.args = args;
    }

    @Override
    public Literal ev() {
        Function func = BUILTIN_FUNCTIONS.get(function);
        if (func != null) {
            if (args.size() != func.getArgumentsCount()) {
                throw new ArgumentException(function, func.getArgumentsCount(), args.size());
            }

            return func.apply(args);
        }

        throw new OperationException(String.format("Function [%s] not found", function));
    }
}