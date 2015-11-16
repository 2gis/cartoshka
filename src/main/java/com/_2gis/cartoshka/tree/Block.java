package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.SymbolTable;
import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Visitor;

import java.util.List;

public class Block extends Node {
    private final List<Node> nodes;

    private final SymbolTable symbolTable;

    public Block(Location location, List<Node> nodes, SymbolTable symbolTable) {
        super(location);
        this.nodes = nodes;
        this.symbolTable = symbolTable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public NodeType type() {
        return NodeType.BLOCK;
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
