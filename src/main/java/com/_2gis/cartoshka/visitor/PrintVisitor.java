package com._2gis.cartoshka.visitor;

import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.entities.*;
import com._2gis.cartoshka.tree.entities.literals.Boolean;
import com._2gis.cartoshka.tree.entities.literals.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

/**
 * Prints the AST to indented source code.
 */
public class PrintVisitor implements Visitor<String, Void> {
    private final StringBuilder out = new StringBuilder();

    private final Stack<Integer> sections = new Stack<>();

    private final int indentSize;

    private int indent = 0;

    private boolean insideExpandableText = false;

    private boolean insideFilter = true;

    public PrintVisitor(int indentSize) {
        this.indentSize = indentSize;
    }

    public PrintVisitor() {
        this(2);
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

    private void visitAll(Collection<? extends Node> nodes, Void params, String delim) {
        Iterator<? extends Node> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(this, params);
            if (iterator.hasNext() && delim != null && !delim.isEmpty()) {
                print(delim);
            }
        }
    }

    @Override
    public String visit(Block block, Void params) {
        enterSection();
        visitAll(block.getNodes(), params, "\n");
        return leaveSection();
    }

    @Override
    public String visit(Ruleset ruleset, Void params) {
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
    public String visit(Rule rule, Void params) {
        enterSection();
        print("%s: ", rule.getFullName());
        rule.getValue().accept(this, params);
        print(";");
        return leaveSection();
    }

    @Override
    public String visit(Selector selector, Void params) {
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
    public String visit(Zoom zoom, Void params) {
        enterSection();
        print("[zoom %s ", zoom.getOperator().getStr());
        zoom.getExpression().accept(this, params);
        print("]");
        return leaveSection();
    }

    @Override
    public String visit(Filter filter, Void params) {
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
    public String visit(Element element, Void params) {
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
    public String visit(Value value, Void params) {
        enterSection();
        visitAll(value.getExpressions(), params, ", ");
        return leaveSection();
    }

    @Override
    public String visit(Variable variable, Void params) {
        enterSection();
        if (insideExpandableText) {
            print("@{%s}", variable.getName().substring(1));
        } else {
            print(variable.getName());
        }

        return leaveSection();
    }

    @Override
    public String visit(UnaryOperation operation, Void params) {
        enterSection();
        print(operation.getOperator().getStr());
        operation.getExpression().accept(this, params);
        return leaveSection();
    }

    @Override
    public String visit(Field field, Void params) {
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
    public String visit(ExpandableText text, Void params) {
        enterSection();
        print("\"");
        insideExpandableText = true;
        visitAll(text.getExpressions(), params, "");
        insideExpandableText = false;
        print("\"");

        return leaveSection();
    }

    @Override
    public String visit(Call call, Void params) {
        enterSection();
        print("%s(", call.getFunction().getName());
        visitAll(call.getArgs(), params, ", ");
        print(")");
        return leaveSection();
    }

    @Override
    public String visit(BinaryOperation operation, Void params) {
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
    public String visit(Boolean value, Void params) {
        enterSection();
        print(value.toString());
        return leaveSection();
    }

    @Override
    public String visit(Color color, Void params) {
        enterSection();
        print(color.toString());
        return leaveSection();
    }

    @Override
    public String visit(Dimension dimension, Void params) {
        enterSection();
        print(dimension.toString());
        return leaveSection();
    }

    @Override
    public String visit(ImageFilter filter, Void params) {
        enterSection();
        print(filter.toString());
        return leaveSection();
    }

    @Override
    public String visit(MultiLiteral multiLiteral, Void params) {
        enterSection();
        print(multiLiteral.toString());
        return leaveSection();
    }

    @Override
    public String visit(Numeric number, Void params) {
        enterSection();
        print(number.toString());
        return leaveSection();
    }

    @Override
    public String visit(Text text, Void params) {
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
