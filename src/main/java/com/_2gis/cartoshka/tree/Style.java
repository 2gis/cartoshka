package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Context;
import com._2gis.cartoshka.Visitor;

import java.util.List;


public class Style extends Node {
    private final List<Node> block;

    private final Context context;

    public Style(List<Node> block, Context context) {
        super(null);
        this.block = block;
        this.context = context;

    }

    public Context getContext() {
        return context;
    }

    public List<Node> getBlock() {
        return block;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitStyle(this, params);
    }

    @Override
    public String toString() {
        return collectionToString(block, "\n");
    }
}