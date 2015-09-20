package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.tree.entities.CompareOperation;

public class Filter {
    private final CompareOperation operation;

    public Filter(CompareOperation operation) {
        this.operation = operation;
    }
}
