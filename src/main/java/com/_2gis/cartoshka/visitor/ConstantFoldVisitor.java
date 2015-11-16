package com._2gis.cartoshka.visitor;

import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.expression.*;
import com._2gis.cartoshka.tree.expression.literal.Boolean;
import com._2gis.cartoshka.tree.expression.literal.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * Traverses the AST and folds constant expressions.
 */
public class ConstantFoldVisitor implements Visitor<Void> {
    private final VolatilityCheckVisitor checkIfVolatile = new VolatilityCheckVisitor();

    private final EvaluateVisitor evaluate = new EvaluateVisitor();

    private void visitAll(Collection<? extends Node> nodes, Void params) {
        for (Node node : nodes) {
            node.accept(this, params);
        }
    }

    private Expression fold(Expression expression, Void params) {
        if (expression.accept(checkIfVolatile, null)) {
            expression.accept(this, params);
            return expression;
        }

        return expression.accept(evaluate, null);
    }

    @Override
    public void visit(Block block, Void params) {
        visitAll(block.getNodes(), params);
    }

    @Override
    public void visit(Ruleset ruleset, Void params) {
        visitAll(ruleset.getSelectors(), params);
        ruleset.getBlock().accept(this, params);
    }

    @Override
    public void visit(Selector selector, Void params) {
        visitAll(selector.getFilters(), params);
        visitAll(selector.getZooms(), params);
    }

    @Override
    public void visit(Zoom zoom, Void params) {
        zoom.setExpression(fold(zoom.getExpression(), params));
    }

    @Override
    public void visit(Filter filter, Void params) {
        filter.setLeft(fold(filter.getLeft(), params));
        filter.setRight(fold(filter.getRight(), params));
    }

    @Override
    public void visit(Element element, Void params) {
        // nothing to do
    }

    @Override
    public void visit(Rule rule, Void params) {
        rule.getValue().accept(this, params);
    }

    // E X P R E S S I O N S

    @Override
    public void visit(Value value, Void params) {
        List<Expression> newExpressions = new ArrayList<>();
        for (Expression expression : value.getExpressions()) {
            newExpressions.add(fold(expression, params));
        }

        value.setExpressions(newExpressions);
    }

    @Override
    public void visit(Variable variable, Void params) {
        variable.getValue().accept(this, params);
    }

    @Override
    public void visit(UnaryOperation operation, Void params) {
        operation.setExpression(fold(operation.getExpression(), params));
    }

    @Override
    public void visit(Field field, Void params) {
        // nothing to do
    }

    @Override
    public void visit(ExpandableText text, Void params) {
        Stack<Expression> stack = new Stack<>();
        for (Expression ex : text.getExpressions()) {
            ex = fold(ex, params);
            if (!stack.isEmpty() && !stack.peek().accept(checkIfVolatile, params) && !ex.accept(checkIfVolatile, params)) {
                Expression nex = stack.pop();
                stack.push(new Text(null, nex.accept(evaluate, null).toString() + ex.accept(evaluate, null).toString(), false, false));
            } else {
                stack.push(ex);
            }
        }

        text.setExpressions(new ArrayList<>(stack));
    }

    @Override
    public void visit(Call call, Void params) {
        List<Expression> newExpressions = new ArrayList<>();
        for (Expression expression : call.getArgs()) {
            newExpressions.add(fold(expression, params));
        }

        call.setArgs(newExpressions);
    }

    @Override
    public void visit(BinaryOperation operation, Void params) {
        operation.setLeft(fold(operation.getLeft(), params));
        operation.setRight(fold(operation.getRight(), params));
    }

    // L I T E R A L S

    @Override
    public void visit(Boolean value, Void params) {
    }

    @Override
    public void visit(Color color, Void params) {
    }

    @Override
    public void visit(Dimension dimension, Void params) {
    }

    @Override
    public void visit(ImageFilter filter, Void params) {
    }

    @Override
    public void visit(MultiLiteral multiLiteral, Void params) {
    }

    @Override
    public void visit(Numeric number, Void params) {
    }

    @Override
    public void visit(Text text, Void params) {
    }
}

