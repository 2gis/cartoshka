package com._2gis.cartoshka.tree;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.expression.Expression;
import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Visitor;

public class Zoom extends Node {
    private final TokenType operator;
    private Expression expression;

    public Zoom(Location location, TokenType operator, Expression expression) {
        super(location);
        this.operator = operator;
        this.expression = expression;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public NodeType type() {
        return NodeType.ZOOM;
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