package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.expression.Value;

public class Rule extends Node {
    private final String instance;
    private final String name;
    private final Value value;
    private final boolean isVariable;
    private final boolean isDefaultInstance;

    public Rule(Location location, String name, Value value, boolean isVariable) {
        super(location);
        String[] parts = name.split("/");
        this.instance = parts.length == 1 ? "__default__" : parts[0];
        this.isDefaultInstance = parts.length == 1 || parts[0].equals("__default__");
        this.name = parts.length == 1 ? name : parts[1];
        this.value = value;
        this.isVariable = isVariable;
    }

    public String getInstance() {
        return instance;
    }

    public String getFullName() {
        return isDefaultInstance ? name : String.format("%s/%s", instance, name);
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public boolean isVariable() {
        return isVariable;
    }

    public boolean isDefaultInstance() {
        return isDefaultInstance;
    }

    @Override
    public NodeType type() {
        return NodeType.RULE;
    }

    @Override
    public <R, P> R accept(GenericVisitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }

    @Override
    public <P> void accept(Visitor<P> visitor, P params) {
        visitor.visit(this, params);
    }
}