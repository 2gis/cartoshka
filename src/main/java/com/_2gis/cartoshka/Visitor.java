package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;

public interface Visitor<R, P> {
    R visitStyle(Style style, P params);

    R visitRuleset(Ruleset ruleset, P params);

    R visitRule(Rule rule, P params);

    R visitSelector(Selector selector, P params);

    R visitZoom(Zoom zoom, P params);

    R visitFilter(Filter filter, P params);

    R visitElement(Element element, P params);

    // E X P R E S S I O N S

    R visitValueExpression(Value value, P params);

    R visitVariableExpression(Variable variable, P params);

    R visitUnaryOperationExpression(UnaryOperation operation, P params);

    R visitFieldExpression(Field field, P params);

    R visitExpandableTextExpression(ExpandableText text, P params);

    R visitCallExpression(Call call, P params);

    R visitBinaryOperationExpression(BinaryOperation operation, P params);

    // L I T E R A L S

    R visitBooleanLiteral(com._2gis.cartoshka.tree.entities.literals.Boolean value, P params);

    R visitColorLiteral(Color color, P params);

    R visitDimensionLiteral(Dimension dimension, P params);

    R visitImageFilterLiteral(ImageFilter filter, P params);

    R visitMultiLiteral(MultiLiteral multiLiteral, P params);

    R visitNumericLiteral(Numeric number, P params);

    R visitTextLiteral(Text text, P params);
}
