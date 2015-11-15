package com._2gis.cartoshka.visitor;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.lang.Boolean;
import java.util.Collection;

public class FilterVisitor implements Visitor<Boolean, Feature> {
    private final EvaluateVisitor evaluate = new EvaluateVisitor();

    private boolean match(Collection<? extends Node> items, Feature feature) {
        for (Node item : items) {
            if (!item.accept(this, feature)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Boolean visit(Block block, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Ruleset ruleset, Feature feature) {
        if (ruleset.getSelectors().isEmpty()) {
            return true;
        }

        for (Selector item : ruleset.getSelectors()) {
            if (item.accept(this, feature)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Boolean visit(Rule rule, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Selector selector, Feature feature) {
        return match(selector.getElements(), feature) && match(selector.getFilters(), feature);
    }

    @Override
    public Boolean visit(Zoom zoom, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Filter filter, Feature feature) {
        Literal lh = filter.getLeft().accept(evaluate, feature);
        Literal rh = filter.getLeft().accept(evaluate, feature);
        switch (filter.getOperator()) {
            case EQ:
                return lh.compareTo(rh) == 0;

            case NE:
                return lh.compareTo(rh) != 0;

            case LT:
                return lh.compareTo(rh) < 0;

            case GT:
                return lh.compareTo(rh) > 0;

            case LTE:
                return lh.compareTo(rh) <= 0;

            case GTE:
                return lh.compareTo(rh) >= 0;
        }

        throw CartoshkaException.invalidOperation(filter.getLocation());
    }

    @Override
    public Boolean visit(Element element, Feature feature) {
        switch (element.getType()) {
            case CLASS:
                if (feature.getClasses().contains(element.getValue())) {
                    return true;
                }

                break;

            case ID:
                if (element.getValue().equals(feature.getLayer())) {
                    return true;
                }

                break;

            case WILDCARD:
                return true;
        }

        return false;
    }

    @Override
    public Boolean visit(Value value, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Variable variable, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(UnaryOperation operation, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Field field, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(ExpandableText text, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Call call, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(BinaryOperation operation, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(com._2gis.cartoshka.tree.entities.literals.Boolean value, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Color color, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Dimension dimension, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(ImageFilter filter, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(MultiLiteral multiLiteral, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Numeric number, Feature feature) {
        return null;
    }

    @Override
    public Boolean visit(Text text, Feature feature) {
        return null;
    }
}
