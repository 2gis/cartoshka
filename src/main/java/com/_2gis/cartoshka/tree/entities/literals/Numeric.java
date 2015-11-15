package com._2gis.cartoshka.tree.entities.literals;

import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.tree.entities.Literal;

public class Numeric extends Literal {
    private final double value;
    private final boolean hasDot;

    public Numeric(Location location, double value, boolean hasDot) {
        super(location);
        this.value = value;
        this.hasDot = hasDot;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Literal operate(TokenType operator) {
        if (operator == TokenType.SUB) {
            return new Numeric(getLocation(), -value, hasDot);
        }

        return super.operate(operator);
    }

    @Override
    public boolean hasDot() {
        return hasDot;
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        if (operand.type() == NodeType.NUMBER) {
            switch (operator) {
                case ADD:
                    return new Numeric(Location.min(getLocation(), operand.getLocation()), value + operand.toNumber(), hasDot || operand.hasDot());
                case SUB:
                    return new Numeric(Location.min(getLocation(), operand.getLocation()), value - operand.toNumber(), hasDot || operand.hasDot());
                case MUL:
                    return new Numeric(Location.min(getLocation(), operand.getLocation()), value * operand.toNumber(), hasDot || operand.hasDot());
                case DIV:
                    return new Numeric(Location.min(getLocation(), operand.getLocation()), value / operand.toNumber(), hasDot || operand.hasDot());
                case MOD:
                    return new Numeric(Location.min(getLocation(), operand.getLocation()), value % operand.toNumber(), hasDot || operand.hasDot());
            }
        } else if (operand.type() == NodeType.TEXT && operator == TokenType.ADD) {
            return new Text(Location.min(getLocation(), operand.getLocation()), toString() + operand.toString(), false, false);
        }

        return super.operate(operator, operand);
    }

    @Override
    public Double toNumber() {
        return value;
    }

    @Override
    public NodeType type() {
        return NodeType.NUMBER;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }

    @Override
    public String toString() {
        if (hasDot) {
            return Double.toString(Math.round(value * 100d) / 100d);
        }

        return Integer.toString((int) value);
    }

    @Override
    public int compareTo(Literal o) {
        if (hasDot) {
            return Long.compare(toNumber().longValue(), o.toNumber().longValue());
        }

        return Double.compare(value, o.toNumber());
    }
}