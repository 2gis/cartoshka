package com._2gis.cartoshka.tree.expression.literal;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.tree.expression.Literal;
import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Visitor;

public class Text extends Literal {
    private final String value;

    private final boolean isURL;

    private final boolean isKeyword;

    public Text(Location location, String value, boolean isURL, boolean isKeyword) {
        super(location);
        this.value = value;
        this.isURL = isURL;
        this.isKeyword = isKeyword;
    }

    @Override
    public NodeType type() {
        return NodeType.TEXT;
    }

    @Override
    public <R, P> R accept(GenericVisitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }

    @Override
    public <P> void accept(Visitor<P> visitor, P params) {
        visitor.visit(this, params);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        if (operator == TokenType.ADD && (operand.type() == NodeType.NUMBER || operand.type() == NodeType.TEXT)) {
            return new Text(Location.min(getLocation(), operand.getLocation()), this.toString() + operand.toString(), isURL, false);
        }

        return super.operate(operator, operand);
    }

    public String getValue() {
        return value;
    }

    public boolean isURL() {
        return isURL;
    }

    public boolean isKeyword() {
        return isKeyword;
    }

    @Override
    public int compareTo(Literal o) {
        return value.compareTo(o.toString());
    }
}