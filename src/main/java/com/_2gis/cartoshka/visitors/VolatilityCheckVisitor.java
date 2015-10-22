package com._2gis.cartoshka.visitors;

import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.lang.Boolean;
import java.util.Collection;

public class VolatilityCheckVisitor implements Visitor<Boolean, Void> {
    private boolean visitAll(Collection<? extends Node> nodes, Void params) {
        for (Node node : nodes) {
            if (node.accept(this, params)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Boolean visitBlock(Block block, Void params) {
        return visitAll(block.getNodes(), params);
    }

    @Override
    public Boolean visitRuleset(Ruleset ruleset, Void params) {
        return visitAll(ruleset.getSelectors(), params) || ruleset.getBlock().accept(this, params);
    }

    @Override
    public Boolean visitRule(Rule rule, Void params) {
        return rule.getValue().accept(this, params);
    }

    @Override
    public Boolean visitSelector(Selector selector, Void params) {
        return visitAll(selector.getFilters(), params) || visitAll(selector.getZooms(), params);
    }

    @Override
    public Boolean visitZoom(Zoom zoom, Void params) {
        return zoom.getExpression().accept(this, params);
    }

    @Override
    public Boolean visitFilter(Filter filter, Void params) {
        return filter.getLeft().accept(this, params) || filter.getRight().accept(this, params);
    }

    @Override
    public Boolean visitElement(Element element, Void params) {
        return false;
    }

    @Override
    public Boolean visitValueExpression(Value value, Void params) {
        return visitAll(value.getExpressions(), params);
    }

    @Override
    public Boolean visitVariableExpression(Variable variable, Void params) {
        return variable.getValue().accept(this, params);
    }

    @Override
    public Boolean visitUnaryOperationExpression(UnaryOperation operation, Void params) {
        return operation.getExpression().accept(this, params);
    }

    @Override
    public Boolean visitFieldExpression(Field field, Void params) {
        return true;
    }

    @Override
    public Boolean visitExpandableTextExpression(ExpandableText text, Void params) {
        return visitAll(text.getExpressions(), params);
    }

    @Override
    public Boolean visitCallExpression(Call call, Void params) {
        return visitAll(call.getArgs(), params);
    }

    @Override
    public Boolean visitBinaryOperationExpression(BinaryOperation operation, Void params) {
        return operation.getLeft().accept(this, params) || operation.getRight().accept(this, params);
    }

    @Override
    public Boolean visitBooleanLiteral(com._2gis.cartoshka.tree.entities.literals.Boolean value, Void params) {
        return false;
    }

    @Override
    public Boolean visitColorLiteral(Color color, Void params) {
        return false;
    }

    @Override
    public Boolean visitDimensionLiteral(Dimension dimension, Void params) {
        return false;
    }

    @Override
    public Boolean visitImageFilterLiteral(ImageFilter filter, Void params) {
        return false;
    }

    @Override
    public Boolean visitMultiLiteral(MultiLiteral multiLiteral, Void params) {
        return false;
    }

    @Override
    public Boolean visitNumericLiteral(Numeric number, Void params) {
        return false;
    }

    @Override
    public Boolean visitTextLiteral(Text text, Void params) {
        return false;
    }
}
