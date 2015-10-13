package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;
import com.sun.istack.internal.Nullable;

public interface Visitor<R, P> {
    R visitRuleset(Ruleset ruleset, @Nullable P params);

    R visitRule(Rule rule, @Nullable P params);

    R visitSelector(Selector selector, @Nullable P params);

    R visitZoom(Zoom zoom, @Nullable P params);

    R visitFilter(Filter filter, @Nullable P params);

    R visitElement(Element element, @Nullable P params);

    // E X P R E S S I O N S

    R visitValue(Value value, @Nullable P params);

    R visitVariable(Variable variable, @Nullable P params);

    R visitUnaryOperation(UnaryOperation operation, @Nullable P params);

    R visitField(Field field, @Nullable P params);

    R visitExpandableText(ExpandableText text, @Nullable P params);

    R visitCall(Call call, @Nullable P params);

    R visitBinaryOperation(BinaryOperation operation, @Nullable P params);

    // L I T E R A L S

    R visitBoolean(com._2gis.cartoshka.tree.entities.literals.Boolean value, @Nullable P params);

    R visitColor(Color color, @Nullable P params);

    R visitDimension(Dimension dimension, @Nullable P params);

    R visitImageFilter(ImageFilter filter, @Nullable P params);

    R visitMultiLiteral(MultiLiteral multiLiteral, @Nullable P params);

    R visitNumber(Numeric number, @Nullable P params);

    R visitText(Text text, @Nullable P params);
}
