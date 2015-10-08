package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.Feature;

public interface Evaluable<T> {
    T ev(Feature feature);
}
