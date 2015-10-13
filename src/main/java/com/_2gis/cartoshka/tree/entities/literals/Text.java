package com._2gis.cartoshka.tree.entities.literals;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.Visitor;
import com._2gis.cartoshka.tree.entities.Literal;

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
    public void accept(Visitor visitor) {
        visitor.visitText(this);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isText() {
        return true;
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        if (operator == TokenType.ADD && (operand.isNumeric() || operand.isText() || operand.isURL())) {
            return new Text(Location.min(getLocation(), operand.getLocation()), this.toString() + operand.toString(), isURL, false);
        }

        return super.operate(operator, operand);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean isURL() {
        return isURL;
    }

    @Override
    public boolean isKeyword() {
        return isKeyword;
    }

    @Override
    public int compareTo(Literal o) {
        return value.compareTo(o.toString());
    }
}