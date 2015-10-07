package com.github.tartakynov.cartoshka.tree;

import java.util.Collection;

public class Selector extends Node {
    private final Collection<Element> elements;
    private final Collection<Filter> filters;
    private final Collection<Zoom> zooms;
    private final String attachment;

    public Selector(Collection<Element> elements, Collection<Filter> filters, Collection<Zoom> zooms, String attachment) {
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
    public void fold() {
        fold(elements);
        fold(filters);
        fold(zooms);
    }
}