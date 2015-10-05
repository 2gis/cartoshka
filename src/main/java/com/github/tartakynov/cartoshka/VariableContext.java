package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Rule;
import com.github.tartakynov.cartoshka.tree.entities.Value;

import java.util.HashMap;

public class VariableContext {
    private final HashMap<String, Value> variables = new HashMap<>();

    private final VariableContext parent;

    public VariableContext() {
        this.parent = null;
    }

    protected VariableContext(VariableContext parent) {
        this.parent = parent;
    }

    public Rule add(Rule variable) {
        if (variable != null && variable.isVariable()) {
            variables.put(variable.getName(), variable.getValue());
            return variable;
        }

        return null;
    }

    public Value get(String name) {
        Value value = variables.get(name);
        if (value == null && parent != null) {
            value = parent.get(name);
        }

        return value;
    }

    public VariableContext createChild() {
        return new VariableContext(this);
    }

    public VariableContext getParent() {
        return parent;
    }

    public boolean hasVariables() {
        return !variables.isEmpty();
    }
}