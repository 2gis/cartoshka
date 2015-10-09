package com._2gis.tree;

import com._2gis.Feature;

public interface Evaluable<T> {
    T ev(Feature feature);
}
