package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Evaluable;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;

import java.util.Collection;

public class Ruleset extends Node implements Evaluable<Boolean> {
    private final Collection<Selector> selectors;
    private final Collection<Node> rules;

    public Ruleset(Location location, Collection<Selector> selectors, Collection<Node> rules) {
        super(location);
        this.selectors = selectors;
        this.rules = rules;
    }

    public Collection<Selector> getSelectors() {
        return selectors;
    }

    public Collection<Node> getRules() {
        return rules;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitRuleset(this, params);
    }

    @Override
    public String toString() {
        return String.format("%s {\n%s\n}", collectionToString(selectors, ", "), collectionToString(rules, "\n"));
    }

    @Override
    public void fold() {
        fold(rules);
        fold(selectors);
    }

    @Override
    public Boolean ev(Feature feature) {
        if (selectors.isEmpty()) {
            return true;
        }

        for (Evaluable<Boolean> item : selectors) {
            if (item.ev(feature)) {
                return true;
            }
        }

        return false;
    }
}