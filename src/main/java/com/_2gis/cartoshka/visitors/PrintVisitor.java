package com._2gis.cartoshka.visitors;

import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.Boolean;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class PrintVisitor implements Visitor<String, Object> {
    private final StringBuilder out = new StringBuilder();

    private final Stack<Integer> sections = new Stack<>();

    private final int indentSize;

    private int indent = 0;

    private boolean insideExpandableText = false;

    private boolean insideFilter = true;

    public PrintVisitor(int indentSize) {
        this.indentSize = indentSize;
    }

    private static boolean needParenthesis(TokenType operator, Expression expression, boolean left) {
        if (expression.type() == NodeType.BINARY_OPERATION) {
            BinaryOperation bop = (BinaryOperation) expression;
            int p0 = operator.getPrecedence();
            int p1 = bop.getOperator().getPrecedence();
            if (p1 < p0 || (p1 == p0 && !left)) {
                return true;
            }
        }

        return false;
    }

    private void print(String format, Object... args) {
        String str = args.length == 0 ? format : String.format(format, args);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            out.append(c);
            if (c == '\n') {
                for (int j = 0; j < indent; j++) {
                    out.append(' ');
                }
            }
        }
    }

    private void enterSection() {
        sections.push(out.length());
    }

    private String leaveSection() {
        String str = out.substring(sections.pop());
        if (sections.isEmpty()) {
            out.setLength(0);
        }

        return str;
    }

    private void increaseIndent() {
        indent += indentSize;
    }

    private void decreaseIndent() {
        indent -= indentSize;
    }

    private void visitAll(Collection<? extends Node> nodes, Object params, String delim) {
        Iterator<? extends Node> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(this, params);
            if (iterator.hasNext() && delim != null && !delim.isEmpty()) {
                print(delim);
            }
        }
    }

    @Override
    public String visitBlock(Block block, Object params) {
        enterSection();
        visitAll(block.getNodes(), params, "\n");
        return leaveSection();
    }

    @Override
    public String visitRuleset(Ruleset ruleset, Object params) {
        enterSection();
        visitAll(ruleset.getSelectors(), params, ",\n");

        increaseIndent();
        print(" {\n");
        ruleset.getBlock().accept(this, params);
        decreaseIndent();
        print("\n}");

        return leaveSection();
    }

    @Override
    public String visitRule(Rule rule, Object params) {
        enterSection();
        print("%s: ", rule.getFullName());
        rule.getValue().accept(this, params);
        print(";");
        return leaveSection();
    }

    @Override
    public String visitSelector(Selector selector, Object params) {
        enterSection();
        visitAll(selector.getElements(), params, "");
        visitAll(selector.getFilters(), params, "");
        visitAll(selector.getZooms(), params, "");
        if (selector.getAttachment() != null && !selector.getAttachment().isEmpty()) {
            print("::%s", selector.getAttachment());
        }

        return leaveSection();
    }

    @Override
    public String visitZoom(Zoom zoom, Object params) {
        enterSection();
        print("[zoom %s ", zoom.getOperator().getStr());
        zoom.getExpression().accept(this, params);
        print("]");
        return leaveSection();
    }

    @Override
    public String visitFilter(Filter filter, Object params) {
        enterSection();
        print("[");
        insideFilter = true;
        filter.getLeft().accept(this, params);
        insideFilter = false;
        print(" %s ", filter.getOperator().getStr());
        filter.getRight().accept(this, params);
        print("]");
        return leaveSection();
    }

    @Override
    public String visitElement(Element element, Object params) {
        enterSection();
        switch (element.getType()) {
            case CLASS:
                print(".");
                break;
            case ID:
                print("#");
                break;
        }

        print(element.getValue());
        return leaveSection();
    }

    @Override
    public String visitValueExpression(Value value, Object params) {
        enterSection();
        visitAll(value.getExpressions(), params, ", ");
        return leaveSection();
    }

    @Override
    public String visitVariableExpression(Variable variable, Object params) {
        enterSection();
        if (insideExpandableText) {
            print("@{%s}", variable.getName().substring(1));
        } else {
            print(variable.getName());
        }

        return leaveSection();
    }

    @Override
    public String visitUnaryOperationExpression(UnaryOperation operation, Object params) {
        enterSection();
        print(operation.getOperator().getStr());
        operation.getExpression().accept(this, params);
        return leaveSection();
    }

    @Override
    public String visitFieldExpression(Field field, Object params) {
        enterSection();
        if (!insideFilter) {
            print("[");
        }

        print("%s", field.getName());
        if (!insideFilter) {
            print("]");
        }

        return leaveSection();
    }

    @Override
    public String visitExpandableTextExpression(ExpandableText text, Object params) {
        enterSection();
        print("\"");
        insideExpandableText = true;
        visitAll(text.getExpressions(), params, "");
        insideExpandableText = false;
        print("\"");

        return leaveSection();
    }

    @Override
    public String visitCallExpression(Call call, Object params) {
        enterSection();
        print("%s(", call.getFunction().getName());
        visitAll(call.getArgs(), params, ", ");
        print(")");
        return leaveSection();
    }

    @Override
    public String visitBinaryOperationExpression(BinaryOperation operation, Object params) {
        enterSection();
        boolean parenthesis = needParenthesis(operation.getOperator(), operation.getLeft(), true);
        if (parenthesis) {
            print("(");
        }
        operation.getLeft().accept(this, params);
        if (parenthesis) {
            print(")");
        }

        print(" %s ", operation.getOperator().getStr());

        parenthesis = needParenthesis(operation.getOperator(), operation.getRight(), false);
        if (parenthesis) {
            print("(");
        }
        operation.getRight().accept(this, params);
        if (parenthesis) {
            print(")");
        }

        return leaveSection();
    }

    @Override
    public String visitBooleanLiteral(Boolean value, Object params) {
        enterSection();
        print(value.toString());
        return leaveSection();
    }

    @Override
    public String visitColorLiteral(Color color, Object params) {
        enterSection();
        print(color.toString());
        return leaveSection();
    }

    @Override
    public String visitDimensionLiteral(Dimension dimension, Object params) {
        enterSection();
        print(dimension.toString());
        return leaveSection();
    }

    @Override
    public String visitImageFilterLiteral(ImageFilter filter, Object params) {
        enterSection();
        print(filter.toString());
        return leaveSection();
    }

    @Override
    public String visitMultiLiteral(MultiLiteral multiLiteral, Object params) {
        enterSection();
        print(multiLiteral.toString());
        return leaveSection();
    }

    @Override
    public String visitNumericLiteral(Numeric number, Object params) {
        enterSection();
        print(number.toString());
        return leaveSection();
    }

    @Override
    public String visitTextLiteral(Text text, Object params) {
        enterSection();
        if (!insideExpandableText && !text.isKeyword()) {
            print("\"");
        }
        print(text.toString());
        if (!insideExpandableText && !text.isKeyword()) {
            print("\"");
        }

        return leaveSection();
    }
}
