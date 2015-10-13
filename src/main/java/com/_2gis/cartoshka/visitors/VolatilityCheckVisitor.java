package com._2gis.cartoshka.visitors;

import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.lang.Boolean;
import java.util.Collection;

public class VolatilityCheckVisitor implements Visitor<Boolean, Object> {
    private boolean visitAll(Collection<? extends Node> nodes, Object params) {
        for (Node node : nodes) {
            if (node.accept(this, params)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Boolean visitStyle(Style style, Object params) {
        return visitAll(style.getBlock(), params);
    }

    @Override
    public Boolean visitRuleset(Ruleset ruleset, Object params) {
        return visitAll(ruleset.getSelectors(), params) || visitAll(ruleset.getBlock(), params);
    }

    @Override
    public Boolean visitRule(Rule rule, Object params) {
        return rule.getValue().accept(this, params);
    }

    @Override
    public Boolean visitSelector(Selector selector, Object params) {
        return visitAll(selector.getFilters(), params) || visitAll(selector.getZooms(), params);
    }

    @Override
    public Boolean visitZoom(Zoom zoom, Object params) {
        return zoom.getExpression().accept(this, params);
    }

    @Override
    public Boolean visitFilter(Filter filter, Object params) {
        return filter.getLeft().accept(this, params) || filter.getRight().accept(this, params);
    }

    @Override
    public Boolean visitElement(Element element, Object params) {
        return false;
    }

    @Override
    public Boolean visitValueExpression(Value value, Object params) {
        return visitAll(value.getExpressions(), params);
    }

    @Override
    public Boolean visitVariableExpression(Variable variable, Object params) {
        return variable.getValue().accept(this, params);
    }

    @Override
    public Boolean visitUnaryOperationExpression(UnaryOperation operation, Object params) {
        return operation.getExpression().accept(this, params);
    }

    @Override
    public Boolean visitFieldExpression(Field field, Object params) {
        return true;
    }

    @Override
    public Boolean visitExpandableTextExpression(ExpandableText text, Object params) {
        return visitAll(text.getExpressions(), params);
    }

    @Override
    public Boolean visitCallExpression(Call call, Object params) {
        return visitAll(call.getArgs(), params);
    }

    @Override
    public Boolean visitBinaryOperationExpression(BinaryOperation operation, Object params) {
        return operation.getLeft().accept(this, params) || operation.getRight().accept(this, params);
    }

    @Override
    public Boolean visitBooleanLiteral(com._2gis.cartoshka.tree.entities.literals.Boolean value, Object params) {
        return false;
    }

    @Override
    public Boolean visitColorLiteral(Color color, Object params) {
        return false;
    }

    @Override
    public Boolean visitDimensionLiteral(Dimension dimension, Object params) {
        return false;
    }

    @Override
    public Boolean visitImageFilterLiteral(ImageFilter filter, Object params) {
        return false;
    }

    @Override
    public Boolean visitMultiLiteral(MultiLiteral multiLiteral, Object params) {
        return false;
    }

    @Override
    public Boolean visitNumericLiteral(Numeric number, Object params) {
        return false;
    }

    @Override
    public Boolean visitTextLiteral(Text text, Object params) {
        return false;
    }
}
