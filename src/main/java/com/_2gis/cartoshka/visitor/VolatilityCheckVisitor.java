package com._2gis.cartoshka.visitor;

import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.expression.*;
import com._2gis.cartoshka.tree.expression.literal.*;

import java.lang.Boolean;
import java.util.Collection;

/**
 * Checks if given branch of the AST is volatile. The branch is volatile if it contains {@link Field}
 * which value depends on {@link com._2gis.cartoshka.Feature}.
 */
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
    public Boolean visit(Block block, Void params) {
        return visitAll(block.getNodes(), params);
    }

    @Override
    public Boolean visit(Ruleset ruleset, Void params) {
        return visitAll(ruleset.getSelectors(), params) || ruleset.getBlock().accept(this, params);
    }

    @Override
    public Boolean visit(Rule rule, Void params) {
        return rule.getValue().accept(this, params);
    }

    @Override
    public Boolean visit(Selector selector, Void params) {
        return visitAll(selector.getFilters(), params) || visitAll(selector.getZooms(), params);
    }

    @Override
    public Boolean visit(Zoom zoom, Void params) {
        return zoom.getExpression().accept(this, params);
    }

    @Override
    public Boolean visit(Filter filter, Void params) {
        return filter.getLeft().accept(this, params) || filter.getRight().accept(this, params);
    }

    @Override
    public Boolean visit(Element element, Void params) {
        return false;
    }

    @Override
    public Boolean visit(Value value, Void params) {
        return visitAll(value.getExpressions(), params);
    }

    @Override
    public Boolean visit(Variable variable, Void params) {
        return variable.getValue().accept(this, params);
    }

    @Override
    public Boolean visit(UnaryOperation operation, Void params) {
        return operation.getExpression().accept(this, params);
    }

    @Override
    public Boolean visit(Field field, Void params) {
        return true;
    }

    @Override
    public Boolean visit(ExpandableText text, Void params) {
        return visitAll(text.getExpressions(), params);
    }

    @Override
    public Boolean visit(Call call, Void params) {
        return visitAll(call.getArgs(), params);
    }

    @Override
    public Boolean visit(BinaryOperation operation, Void params) {
        return operation.getLeft().accept(this, params) || operation.getRight().accept(this, params);
    }

    @Override
    public Boolean visit(com._2gis.cartoshka.tree.expression.literal.Boolean value, Void params) {
        return false;
    }

    @Override
    public Boolean visit(Color color, Void params) {
        return false;
    }

    @Override
    public Boolean visit(Dimension dimension, Void params) {
        return false;
    }

    @Override
    public Boolean visit(ImageFilter filter, Void params) {
        return false;
    }

    @Override
    public Boolean visit(MultiLiteral multiLiteral, Void params) {
        return false;
    }

    @Override
    public Boolean visit(Numeric number, Void params) {
        return false;
    }

    @Override
    public Boolean visit(Text text, Void params) {
        return false;
    }
}
