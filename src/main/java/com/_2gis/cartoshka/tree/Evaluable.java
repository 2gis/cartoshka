package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Feature;

public interface Evaluable<T> {
    T ev(Feature feature);
}
