package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.expression.*;
import com._2gis.cartoshka.tree.expression.literal.*;

/**
 * A visitor without return value.
 * @param <P> Type of parameter.
 */
public interface Visitor<P> {
    void visit(Block block, P params);

    void visit(Ruleset ruleset, P params);

    void visit(Selector selector, P params);

    void visit(Zoom zoom, P params);

    void visit(Filter filter, P params);

    void visit(Element element, P params);

    void visit(Rule rule, P params);

    // E X P R E S S I O N S

    void visit(Value value, P params);

    void visit(Variable variable, P params);

    void visit(UnaryOperation operation, P params);

    void visit(Field field, P params);

    void visit(ExpandableText text, P params);

    void visit(Call call, P params);

    void visit(BinaryOperation operation, P params);

    // L I T E R A L S

    void visit(com._2gis.cartoshka.tree.expression.literal.Boolean value, P params);

    void visit(Color color, P params);

    void visit(Dimension dimension, P params);

    void visit(ImageFilter filter, P params);

    void visit(MultiLiteral multiLiteral, P params);

    void visit(Numeric number, P params);

    void visit(Text text, P params);
}
