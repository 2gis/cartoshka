package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

import java.util.Collection;

public class Selector extends Node {
    private final Collection<Element> elements;
    private final Collection<Filter> filters;
    private final Collection<Zoom> zooms;
    private final String attachment;

    public Selector(Location location, Collection<Element> elements, Collection<Filter> filters, Collection<Zoom> zooms, String attachment) {
        super(location);
        this.elements = elements;
        this.filters = filters;
        this.zooms = zooms;
        this.attachment = attachment;
    }

    public Collection<Element> getElements() {
        return elements;
    }

    public Collection<Filter> getFilters() {
        return filters;
    }

    public Collection<Zoom> getZooms() {
        return zooms;
    }

    public String getAttachment() {
        return attachment;
    }

    @Override
    public NodeType type() {
        return NodeType.SELECTOR;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }
}