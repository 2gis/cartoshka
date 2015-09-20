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
}
