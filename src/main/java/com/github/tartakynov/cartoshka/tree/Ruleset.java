package com.github.tartakynov.cartoshka.tree;

import java.util.Collection;

public class Ruleset extends Node {
    private final Collection<Selector> selectors;
    private final Collection<Node> rules;

    public Ruleset(Collection<Selector> selectors, Collection<Node> rules) {
        this.selectors = selectors;
        this.rules = rules;
    }
}
