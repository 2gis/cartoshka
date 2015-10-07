package com.github.tartakynov.cartoshka.tree;

import java.util.Collection;

public class Ruleset extends Node {
    private final Collection<Selector> selectors;
    private final Collection<Node> rules;

    public Ruleset(Collection<Selector> selectors, Collection<Node> rules) {
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
}
