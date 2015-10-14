package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.SymbolTable;
import com._2gis.cartoshka.Visitor;

import java.util.List;


public class Style extends Node {
    private final List<Node> block;

    private final SymbolTable symbolTable;

    public Style(List<Node> block, SymbolTable symbolTable) {
        super(null);
        this.block = block;
        this.symbolTable = symbolTable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<Node> getBlock() {
        return block;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitStyle(this, params);
    }
}