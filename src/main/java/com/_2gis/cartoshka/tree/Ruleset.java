package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Visitor;

import java.util.List;

public class Ruleset extends Node {
    private final List<Selector> selectors;
    private final Block block;

    public Ruleset(Location location, List<Selector> selectors, Block block) {
        super(location);
        this.selectors = selectors;
        this.block = block;
    }

    public List<Selector> getSelectors() {
        return selectors;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public NodeType type() {
        return NodeType.RULESET;
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