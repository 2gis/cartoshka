package com._2gis.cartoshka.visitors;

import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.Boolean;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class EvaluatingVisitor implements Visitor<Expression, Object> {
    private final DynamicityCheckVisitor dynamicityCheck = new DynamicityCheckVisitor();

    private void visitAll(Collection<? extends Node> nodes, Object params) {
        for (Node node : nodes) {
            node.accept(this, params);
        }
    }

    @Override
    public Expression visitStyle(Style style, Object params) {
        visitAll(style.getBlock(), params);
        return null;
    }

    @Override
    public Expression visitRuleset(Ruleset ruleset, Object params) {
        visitAll(ruleset.getSelectors(), params);
        visitAll(ruleset.getBlock(), params);
        return null;
    }

    @Override
    public Expression visitRule(Rule rule, Object params) {
        rule.getValue().accept(this, params);
        return null;
    }

    @Override
    public Expression visitElement(Element element, Object params) {
        return null;
    }

    @Override
    public Expression visitSelector(Selector selector, Object params) {
        visitAll(selector.getFilters(), params);
        visitAll(selector.getZooms(), params);
        return null;
    }

    @Override
    public Expression visitZoom(Zoom zoom, Object params) {
        zoom.setExpression(zoom.getExpression().accept(this, params));
        return null;
    }

    @Override
    public Expression visitFilter(Filter filter, Object params) {
        filter.setLeft(filter.getLeft().accept(this, params));
        filter.setRight(filter.getRight().accept(this, params));
        return null;
    }

    // E X P R E S S I O N S

    @Override
    public Expression visitValueExpression(Value value, Object params) {
        List<Expression> newExpressions = new ArrayList<>();
        for (Expression expression : value.getExpressions()) {
            newExpressions.add(expression.accept(this, params));
        }

        value.setExpressions(newExpressions);
        return value;
    }

    @Override
    public Expression visitVariableExpression(Variable variable, Object params) {
        if (variable.accept(dynamicityCheck, params)) {
            variable.getValue().accept(this, params);
            return variable;
        }

        return variable.ev(null);
    }

    @Override
    public Expression visitUnaryOperationExpression(UnaryOperation operation, Object params) {
        if (operation.accept(dynamicityCheck, params)) {
            operation.setExpression(operation.getExpression().accept(this, params));
            return operation;
        }

        return operation.ev(null);
    }

    @Override
    public Expression visitFieldExpression(Field field, Object params) {
        return field;
    }

    @Override
    public Expression visitExpandableTextExpression(ExpandableText text, Object params) {
        if (text.accept(dynamicityCheck, params)) {
            Stack<Expression> stack = new Stack<>();
            for (Expression ex : text.getExpressions()) {
                ex = ex.accept(this, params);
                if (!stack.isEmpty() && !stack.peek().accept(dynamicityCheck, params) && !ex.accept(dynamicityCheck, params)) {
                    Expression nex = stack.pop();
                    stack.push(new Text(null, nex.ev(null).toString() + ex.ev(null).toString(), false, false));
                } else {
                    stack.push(ex);
                }
            }

            text.setExpressions(new ArrayList<>(stack));
            return text;
        }

        return text.ev(null);
    }

    @Override
    public Expression visitCallExpression(Call call, Object params) {
        if (call.accept(dynamicityCheck, params)) {
            List<Expression> newArgs = new ArrayList<>();
            for (Expression arg : call.getArgs()) {
                newArgs.add(arg.accept(this, params));
            }

            call.setArgs(newArgs);
            return call;
        }

        return call.ev(null);
    }

    @Override
    public Expression visitBinaryOperationExpression(BinaryOperation operation, Object params) {
        if (operation.accept(dynamicityCheck, params)) {
            operation.setLeft(operation.getLeft().accept(this, params));
            operation.setRight(operation.getRight().accept(this, params));
            return operation;
        }

        return operation.ev(null);
    }

    // L I T E R A L S

    @Override
    public Expression visitBooleanLiteral(Boolean value, Object params) {
        return value;
    }

    @Override
    public Expression visitColorLiteral(Color color, Object params) {
        return color;
    }

    @Override
    public Expression visitDimensionLiteral(Dimension dimension, Object params) {
        return dimension;
    }

    @Override
    public Expression visitImageFilterLiteral(ImageFilter filter, Object params) {
        return filter;
    }

    @Override
    public Expression visitMultiLiteral(MultiLiteral multiLiteral, Object params) {
        return multiLiteral;
    }

    @Override
    public Expression visitNumericLiteral(Numeric number, Object params) {
        return number;
    }

    @Override
    public Expression visitTextLiteral(Text text, Object params) {
        return text;
    }
}
