package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.expression.*;
import com._2gis.cartoshka.tree.expression.literal.*;

/**
 * A visitor with return value.
 *
 * @param <R> Type of return type.
 * @param <P> Type of parameter.
 */
public interface GenericVisitor<R, P> {
    R visit(Block block, P params);

    R visit(Ruleset ruleset, P params);

    R visit(Selector selector, P params);

    R visit(Zoom zoom, P params);

    R visit(Filter filter, P params);

    R visit(Element element, P params);

    R visit(Rule rule, P params);

    // E X P R E S S I O N S

    R visit(Value value, P params);

    R visit(Variable variable, P params);

    R visit(UnaryOperation operation, P params);

    R visit(Field field, P params);

    R visit(ExpandableText text, P params);

    R visit(Call call, P params);

    R visit(BinaryOperation operation, P params);

    // L I T E R A L S

    R visit(com._2gis.cartoshka.tree.expression.literal.Boolean value, P params);

    R visit(Color color, P params);

    R visit(Dimension dimension, P params);

    R visit(ImageFilter filter, P params);

    R visit(MultiLiteral multiLiteral, P params);

    R visit(Numeric number, P params);

    R visit(Text text, P params);
}
