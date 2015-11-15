package com._2gis.cartoshka.visitor;

import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.Boolean;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * Traversing the AST and folds constant expressions.
 */
public class ConstantFoldVisitor implements Visitor<Expression, Void> {
    private final VolatilityCheckVisitor checkIfVolatile = new VolatilityCheckVisitor();

    private final EvaluateVisitor evaluate = new EvaluateVisitor();

    private void visitAll(Collection<? extends Node> nodes, Void params) {
        for (Node node : nodes) {
            node.accept(this, params);
        }
    }

    @Override
    public Expression visit(Block block, Void params) {
        visitAll(block.getNodes(), params);
        return null;
    }

    @Override
    public Expression visit(Ruleset ruleset, Void params) {
        visitAll(ruleset.getSelectors(), params);
        ruleset.getBlock().accept(this, params);
        return null;
    }

    @Override
    public Expression visit(Rule rule, Void params) {
        rule.getValue().accept(this, params);
        return null;
    }

    @Override
    public Expression visit(Element element, Void params) {
        return null;
    }

    @Override
    public Expression visit(Selector selector, Void params) {
        visitAll(selector.getFilters(), params);
        visitAll(selector.getZooms(), params);
        return null;
    }

    @Override
    public Expression visit(Zoom zoom, Void params) {
        zoom.setExpression(zoom.getExpression().accept(this, params));
        return null;
    }

    @Override
    public Expression visit(Filter filter, Void params) {
        filter.setLeft(filter.getLeft().accept(this, params));
        filter.setRight(filter.getRight().accept(this, params));
        return null;
    }

    // E X P R E S S I O N S

    @Override
    public Expression visit(Value value, Void params) {
        List<Expression> newExpressions = new ArrayList<>();
        for (Expression expression : value.getExpressions()) {
            newExpressions.add(expression.accept(this, params));
        }

        value.setExpressions(newExpressions);
        return value;
    }

    @Override
    public Expression visit(Variable variable, Void params) {
        if (variable.accept(checkIfVolatile, params)) {
            variable.getValue().accept(this, params);
            return variable;
        }

        return variable.accept(evaluate, null);
    }

    @Override
    public Expression visit(UnaryOperation operation, Void params) {
        if (operation.accept(checkIfVolatile, params)) {
            operation.setExpression(operation.getExpression().accept(this, params));
            return operation;
        }

        return operation.accept(evaluate, null);
    }

    @Override
    public Expression visit(Field field, Void params) {
        return field;
    }

    @Override
    public Expression visit(ExpandableText text, Void params) {
        if (text.accept(checkIfVolatile, params)) {
            Stack<Expression> stack = new Stack<>();
            for (Expression ex : text.getExpressions()) {
                ex = ex.accept(this, params);
                if (!stack.isEmpty() && !stack.peek().accept(checkIfVolatile, params) && !ex.accept(checkIfVolatile, params)) {
                    Expression nex = stack.pop();
                    stack.push(new Text(null, nex.accept(evaluate, null).toString() + ex.accept(evaluate, null).toString(), false, false));
                } else {
                    stack.push(ex);
                }
            }

            text.setExpressions(new ArrayList<>(stack));
            return text;
        }

        return text.accept(evaluate, null);
    }

    @Override
    public Expression visit(Call call, Void params) {
        if (call.accept(checkIfVolatile, params)) {
            List<Expression> newArgs = new ArrayList<>();
            for (Expression arg : call.getArgs()) {
                newArgs.add(arg.accept(this, params));
            }

            call.setArgs(newArgs);
            return call;
        }

        return call.accept(evaluate, null);
    }

    @Override
    public Expression visit(BinaryOperation operation, Void params) {
        if (operation.accept(checkIfVolatile, params)) {
            operation.setLeft(operation.getLeft().accept(this, params));
            operation.setRight(operation.getRight().accept(this, params));
            return operation;
        }

        return operation.accept(evaluate, null);
    }

    // L I T E R A L S

    @Override
    public Expression visit(Boolean value, Void params) {
        return value;
    }

    @Override
    public Expression visit(Color color, Void params) {
        return color;
    }

    @Override
    public Expression visit(Dimension dimension, Void params) {
        return dimension;
    }

    @Override
    public Expression visit(ImageFilter filter, Void params) {
        return filter;
    }

    @Override
    public Expression visit(MultiLiteral multiLiteral, Void params) {
        return multiLiteral;
    }

    @Override
    public Expression visit(Numeric number, Void params) {
        return number;
    }

    @Override
    public Expression visit(Text text, Void params) {
        return text;
    }
}
