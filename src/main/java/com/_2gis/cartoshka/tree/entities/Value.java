package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.NodeType;

import java.util.Collection;

public class Value extends Expression {
    private Collection<Expression> expressions;

    public Value(Location location, Collection<Expression> expressions) {
        super(location);
        this.expressions = expressions;
    }

    @Override
    public NodeType type() {
        return NodeType.VALUE;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitValueExpression(this, params);
    }

    public Collection<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(Collection<Expression> expressions) {
        this.expressions = expressions;
    }
}