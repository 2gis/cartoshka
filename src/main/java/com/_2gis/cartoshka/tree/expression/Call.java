package com._2gis.cartoshka.tree.expression;

import com._2gis.cartoshka.Function;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Visitor;

import java.util.Collection;

public class Call extends Expression {
    private final Function function;
    private Collection<Expression> args;

    public Call(Location location, Function function, Collection<Expression> args) {
        super(location);
        this.function = function;
        this.args = args;
    }

    public Function getFunction() {
        return function;
    }

    public Collection<Expression> getArgs() {
        return args;
    }

    public void setArgs(Collection<Expression> args) {
        this.args = args;
    }

    @Override
    public NodeType type() {
        return NodeType.CALL;
    }

    @Override
    public <R, P> R accept(GenericVisitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }

    @Override
    public <P> void accept(Visitor<P> visitor, P params) {
        visitor.visit(this, params);
    }
}