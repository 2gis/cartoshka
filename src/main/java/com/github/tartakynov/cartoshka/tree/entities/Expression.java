package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.tree.Node;

public abstract class Expression extends Node {
    public abstract Literal ev();
}
