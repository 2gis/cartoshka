package com.github.tartakynov.cartoshka.tree.entities.literals;

import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.scanner.TokenType;
import com.github.tartakynov.cartoshka.tree.entities.Literal;

public class Dimension extends Literal {
    private final double value;

    private final String unit;

    public Dimension(double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public Literal operate(TokenType operator) {
        return new Dimension(-value, unit);
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        if (operand.isDimension() || operand.isNumeric()) {
            Double left = this.getValue();
            Double right = null;
            if (operand.isDimension()) {
                Dimension dimension = (Dimension) operand;
                if (unit.equals(dimension.getUnit())) {
                    right = dimension.getValue();
                }
            } else {
                right = operand.toNumber();
            }

            if (right != null) {
                switch (operator) {
                    case ADD:
                        return new Dimension(left + right, unit);
                    case SUB:
                        return new Dimension(left - right, unit);
                    case MUL:
                        return new Dimension(left * right, unit);
                    case DIV:
                        return new Dimension(left / right, unit);
                    case MOD:
                        return new Dimension(left % right, unit);
                }
            }
        }

        return super.operate(operator, operand);
    }

    @Override
    public boolean isDimension() {
        return true;
    }

    @Override
    public Double toNumber() {
        if (unit.equals("%")) {
            return value / 100.0;
        }

        return super.toNumber();
    }

    @Override
    public boolean hasDot() {
        return unit.equals("%");
    }

    @Override
    public String toString() {
        return String.format("%s%s", Double.toString(value), unit);
    }

    @Override
    public int compareTo(Literal o) {
        if (o.isDimension()) {
            Dimension other = (Dimension) o;
            if (other.getUnit().equals(unit)) {
                return Double.compare(value, other.getValue());
            }
        }

        throw CartoshkaException.incorrectComparison(this);
    }
}