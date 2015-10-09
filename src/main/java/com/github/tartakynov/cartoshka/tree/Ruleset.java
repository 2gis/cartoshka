package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.Location;

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
