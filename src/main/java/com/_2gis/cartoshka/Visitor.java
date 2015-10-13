package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;

public interface Visitor<R, P> {
    R visitRuleset(Ruleset ruleset, P params);

    R visitRule(Rule rule, P params);

    R visitSelector(Selector selector, P params);

    R visitZoom(Zoom zoom, P params);

    R visitFilter(Filter filter, P params);

    R visitElement(Element element, P params);

    // E X P R E S S I O N S

    R visitValue(Value value, P params);

    R visitVariable(Variable variable, P params);

    R visitUnaryOperation(UnaryOperation operation, P params);

    R visitField(Field field, P params);

    R visitExpandableText(ExpandableText text, P params);

    R visitCall(Call call, P params);

    R visitBinaryOperation(BinaryOperation operation, P params);

    // L I T E R A L S

    R visitBoolean(com._2gis.cartoshka.tree.entities.literals.Boolean value, P params);

    R visitColor(Color color, P params);

    R visitDimension(Dimension dimension, P params);

    R visitImageFilter(ImageFilter filter, P params);

    R visitMultiLiteral(MultiLiteral multiLiteral, P params);

    R visitNumber(Numeric number, P params);

    R visitText(Text text, P params);
}
