package com._2gis.cartoshka.tree.expression;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.SymbolTable;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.NodeType;

public class Variable extends Expression {
    private final String name;
    private final SymbolTable symbolTable;
    private Value value;

    public Variable(Location location, SymbolTable symbolTable, String name) {
        super(location);
        this.symbolTable = symbolTable;
        this.name = name;
    }

    @Override
    public NodeType type() {
        return NodeType.VARIABLE;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        if (value == null) {
            value = symbolTable.getVariable(name);
            if (value == null) {
                throw CartoshkaException.undefinedVariable(getLocation());
            }
        }

        return value;
    }
}