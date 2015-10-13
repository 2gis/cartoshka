package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Evaluable;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

import java.util.Collection;

public class Selector extends Node implements Evaluable<Boolean> {
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

    private static boolean match(Collection<? extends Evaluable<Boolean>> items, Feature feature) {
        for (Evaluable<Boolean> item : items) {
            if (!item.ev(feature)) {
                return false;
            }
        }

        return true;
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

    public Boolean ev(Feature feature) {
        return match(elements, feature) && match(filters, feature);
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitSelector(this, params);
    }

    @Override
    public String toString() {
        return String.format("%s%s%s%s", collectionToString(elements, " "), collectionToString(filters, ""), collectionToString(zooms, ""), attachment);
    }
}