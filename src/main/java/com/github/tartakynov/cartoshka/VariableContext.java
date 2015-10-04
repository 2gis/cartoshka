package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Rule;

import java.util.HashMap;

public class VariableContext {
    private final HashMap<String, Rule> variables = new HashMap<>();

    private final VariableContext parent;

    public VariableContext() {
        this.parent = null;
    }

    protected VariableContext(VariableContext parent) {
        this.parent = parent;
    }

    public Rule add(Rule variable) {
        if (variable != null && variable.isVariable()) {
            variables.put(variable.getName(), variable);
            return variable;
        }

        return null;
    }

    public Rule get(String name) {
        Rule variable = variables.get(name);
        if (variable == null && parent != null) {
            variable = parent.get(name);
        }

        return variable;
    }

    public VariableContext createChild() {
        return new VariableContext(this);
    }

    public VariableContext getParent() {
        return parent;
    }
}