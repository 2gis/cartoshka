package com._2gis.cartoshka.visitor;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Evaluates given {@link Expression} and returns {@link Literal} it represents.
 */
public class EvaluateVisitor implements Visitor<Literal, Feature> {

    @Override
    public Literal visit(Block block, Feature feature) {
        return null;
    }

    @Override
    public Literal visit(Ruleset ruleset, Feature feature) {
        return null;
    }

    @Override
    public Literal visit(Rule rule, Feature feature) {
        return null;
    }

    @Override
    public Literal visit(Selector selector, Feature feature) {
        return null;
    }

    @Override
    public Literal visit(Zoom zoom, Feature feature) {
        return null;
    }

    @Override
    public Literal visit(Filter filter, Feature feature) {
        return null;
    }

    @Override
    public Literal visit(Element element, Feature feature) {
        return null;
    }

    // E X P R E S S I O N S

    @Override
    public Literal visit(Value value, Feature feature) {
        if (value.getExpressions().size() == 1) {
            return value.getExpressions().iterator().next().accept(this, feature);
        }

        List<Literal> literals = new ArrayList<>();
        for (Expression expression : value.getExpressions()) {
            literals.add(expression.accept(this, feature));
        }

        return new MultiLiteral(value.getLocation(), literals);
    }

    @Override
    public Literal visit(Variable variable, Feature feature) {
        return variable.getValue().accept(this, feature);
    }

    @Override
    public Literal visit(UnaryOperation operation, Feature feature) {
        return operation.getExpression().accept(this, feature).operate(operation.getOperator());
    }

    @Override
    public Literal visit(Field field, Feature feature) {
        if (feature == null) {
            throw CartoshkaException.featureIsNotProvided(field.getLocation());
        }

        Literal literal = feature.getField(field.getName());
        if (literal == null) {
            throw CartoshkaException.fieldNotFound(field.getLocation());
        }

        return literal;
    }

    @Override
    public Literal visit(ExpandableText text, Feature feature) {
        StringBuilder builder = new StringBuilder();
        for (Expression expression : text.getExpressions()) {
            builder.append(expression.accept(this, feature).toString());
        }

        return new Text(text.getLocation(), builder.toString(), text.isURL(), false);
    }

    @Override
    public Literal visit(final Call call, final Feature feature) {
        final EvaluateVisitor ev = this;
        return call.getFunction().apply(call.getLocation(), new Iterator<Literal>() {
            Iterator<Expression> iterator = call.getArgs().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Literal next() {
                return iterator.next().accept(ev, feature);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Override
    public Literal visit(BinaryOperation operation, Feature feature) {
        Literal leftOp = operation.getLeft().accept(this, feature);
        Literal rightOp = operation.getRight().accept(this, feature);
        if ((leftOp.type() != NodeType.COLOR && rightOp.type() == NodeType.COLOR) || (leftOp.type() == NodeType.NUMBER && rightOp.type() == NodeType.DIMENSION)) {
            Literal tmp = leftOp;
            leftOp = rightOp;
            rightOp = tmp;
        }

        return leftOp.operate(operation.getOperator(), rightOp);
    }

    // L I T E R A L S

    @Override
    public Literal visit(com._2gis.cartoshka.tree.entities.literals.Boolean value, Feature feature) {
        return value;
    }

    @Override
    public Literal visit(Color color, Feature feature) {
        return color;
    }

    @Override
    public Literal visit(Dimension dimension, Feature feature) {
        return dimension;
    }

    @Override
    public Literal visit(ImageFilter filter, Feature feature) {
        return filter;
    }

    @Override
    public Literal visit(MultiLiteral multiLiteral, Feature feature) {
        return multiLiteral;
    }

    @Override
    public Literal visit(Numeric number, Feature feature) {
        return number;
    }

    @Override
    public Literal visit(Text text, Feature feature) {
        return text;
    }
}
