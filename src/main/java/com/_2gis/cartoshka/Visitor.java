package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.*;

public interface Visitor {
    void visitRuleset(Ruleset ruleset);

    void visitRule(Rule rule);

    void visitSelector(Selector selector);

    void visitZoom(Zoom zoom);

    void visitFilter(Filter filter);

    void visitElement(Element element);

    // E X P R E S S I O N S

    void visitValue(Value value);

    void visitVariable(Variable variable);

    void visitUnaryOperation(UnaryOperation operation);

    void visitField(Field field);

    void visitExpandableText(ExpandableText text);

    void visitCall(Call call);

    void visitBinaryOperation(BinaryOperation operation);

    // L I T E R A L S

    void visitBoolean(com._2gis.cartoshka.tree.entities.literals.Boolean value);

    void visitColor(Color color);

    void visitDimension(Dimension dimension);

    void visitImageFilter(ImageFilter filter);

    void visitMultiLiteral(MultiLiteral multiLiteral);

    void visitNumber(Numeric number);

    void visitText(Text text);
}
