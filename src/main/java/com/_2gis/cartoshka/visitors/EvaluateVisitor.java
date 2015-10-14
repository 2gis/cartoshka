package com._2gis.cartoshka.visitors;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EvaluateVisitor implements Visitor<Literal, Feature> {

    @Override
    public Literal visitBlock(Block block, Feature feature) {
        return null;
    }

    @Override
    public Literal visitRuleset(Ruleset ruleset, Feature feature) {
        return null;
    }

    @Override
    public Literal visitRule(Rule rule, Feature feature) {
        return null;
    }

    @Override
    public Literal visitSelector(Selector selector, Feature feature) {
        return null;
    }

    @Override
    public Literal visitZoom(Zoom zoom, Feature feature) {
        return null;
    }

    @Override
    public Literal visitFilter(Filter filter, Feature feature) {
        return null;
    }

    @Override
    public Literal visitElement(Element element, Feature feature) {
        return null;
    }

    // E X P R E S S I O N S

    @Override
    public Literal visitValueExpression(Value value, Feature feature) {
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
    public Literal visitVariableExpression(Variable variable, Feature feature) {
        return variable.getValue().accept(this, feature);
    }

    @Override
    public Literal visitUnaryOperationExpression(UnaryOperation operation, Feature feature) {
        return operation.getExpression().accept(this, feature).operate(operation.getOperator());
    }

    @Override
    public Literal visitFieldExpression(Field field, Feature feature) {
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
    public Literal visitExpandableTextExpression(ExpandableText text, Feature feature) {
        StringBuilder builder = new StringBuilder();
        for (Expression expression : text.getExpressions()) {
            builder.append(expression.accept(this, feature).toString());
        }

        return new Text(text.getLocation(), builder.toString(), text.isURL(), false);
    }

    @Override
    public Literal visitCallExpression(final Call call, final Feature feature) {
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
    public Literal visitBinaryOperationExpression(BinaryOperation operation, Feature feature) {
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
    public Literal visitBooleanLiteral(com._2gis.cartoshka.tree.entities.literals.Boolean value, Feature feature) {
        return value;
    }

    @Override
    public Literal visitColorLiteral(Color color, Feature feature) {
        return color;
    }

    @Override
    public Literal visitDimensionLiteral(Dimension dimension, Feature feature) {
        return dimension;
    }

    @Override
    public Literal visitImageFilterLiteral(ImageFilter filter, Feature feature) {
        return filter;
    }

    @Override
    public Literal visitMultiLiteral(MultiLiteral multiLiteral, Feature feature) {
        return multiLiteral;
    }

    @Override
    public Literal visitNumericLiteral(Numeric number, Feature feature) {
        return number;
    }

    @Override
    public Literal visitTextLiteral(Text text, Feature feature) {
        return text;
    }
}
