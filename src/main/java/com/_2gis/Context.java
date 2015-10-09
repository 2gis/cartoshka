package com._2gis;

import com._2gis.tree.Rule;
import com._2gis.tree.entities.Value;

import java.util.HashMap;

public class Context {
    private final HashMap<String, Value> variables = new HashMap<>();

    private final Context parent;

    public Context() {
        this.parent = null;
    }

    protected Context(Context parent) {
        this.parent = parent;
    }

    public Rule setVariable(Rule variable) {
        if (variable != null && variable.isVariable()) {
            variables.put(variable.getName(), variable.getValue());
            return variable;
        }

        return null;
    }

    public Value getVariable(String name) {
        Value value = variables.get(name);
        if (value == null && parent != null) {
            value = parent.getVariable(name);
        }

        return value;
    }

    public Context createNestedBlockContext() {
        return new Context(this);
    }

    public Context getParent() {
        return parent;
    }

    public boolean hasVariables() {
        return !variables.isEmpty();
    }
}