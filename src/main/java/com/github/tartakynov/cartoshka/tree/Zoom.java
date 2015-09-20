package com.github.tartakynov.cartoshka.tree;

import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Expression;

public class Zoom extends Filter {
    public Zoom(TokenType operator, Expression left, Expression right) {
        super(operator, left, right);
    }
}
