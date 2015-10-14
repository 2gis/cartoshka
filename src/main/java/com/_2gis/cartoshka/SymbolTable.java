package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.Rule;
import com._2gis.cartoshka.tree.entities.Value;

import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, Value> variables = new HashMap<>();

    private final SymbolTable parent;

    public SymbolTable() {
        this.parent = null;
    }

    protected SymbolTable(SymbolTable parent) {
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

    public SymbolTable createNested() {
        return new SymbolTable(this);
    }

    public SymbolTable getParent() {
        return parent;
    }

    public boolean hasVariables() {
        return !variables.isEmpty();
    }
}