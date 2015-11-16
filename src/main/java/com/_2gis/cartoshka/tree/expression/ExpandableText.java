package com._2gis.cartoshka.tree.expression;

import com._2gis.cartoshka.GenericVisitor;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.SymbolTable;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.NodeType;
import com._2gis.cartoshka.tree.expression.literal.Text;

import java.util.LinkedList;
import java.util.List;

public class ExpandableText extends Expression {
    private final SymbolTable symbolTable;
    private final boolean isURL;
    private List<Expression> expressions;

    public ExpandableText(Location location, SymbolTable symbolTable, String value, boolean isURL) {
        super(location);
        this.symbolTable = symbolTable;
        this.isURL = isURL;
        this.expressions = parse(value);
    }

    public boolean isURL() {
        return isURL;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    private List<Expression> parse(String value) {
        int size = value.length();
        int start = 0;
        List<Expression> expressions = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        while (start < size) {
            switch (value.charAt(start)) {
                case '\\':
                    start++;
                    break;

                case '@':
                    if ((start + 1) < size && value.charAt(start + 1) == '{') {
                        start += 2;
                        int end = value.indexOf('}', start);
                        if (start < end) {
                            String name = value.substring(start, end);
                            if (sb.length() > 0) {
                                expressions.add(new Text(getLocation(), sb.toString(), isURL, false));
                                sb.setLength(0);
                            }

                            expressions.add(new Variable(getSubLocation(start), symbolTable, "@" + name));
                        }

                        start = end + 1;
                    }

                    break;

                case '[':
                    start++;
                    int end = value.indexOf(']', start);
                    if (start < end) {
                        String name = value.substring(start, end);
                        if (sb.length() > 0) {
                            expressions.add(new Text(getLocation(), sb.toString(), isURL, false));
                            sb.setLength(0);
                        }

                        expressions.add(new Field(getSubLocation(start), name));
                    }

                    start = end + 1;
                    break;
            }

            if (start < size) {
                sb.append(value.charAt(start));
                start++;
            }
        }

        if (sb.length() > 0) {
            expressions.add(new Text(getLocation(), sb.toString(), isURL, false));
        }

        return expressions;
    }

    @Override
    public NodeType type() {
        return NodeType.EXPANDABLE_TEXT;
    }

    @Override
    public <R, P> R accept(GenericVisitor<R, P> visitor, P params) {
        return visitor.visit(this, params);
    }

    @Override
    public <P> void accept(Visitor<P> visitor, P params) {
        visitor.visit(this, params);
    }

    public boolean isPlain() {
        return expressions.size() == 1 && (expressions.get(0).type() == NodeType.TEXT);
    }

    private Location getSubLocation(int start) {
        return getLocation() == null
                ? null
                : new Location(getLocation().name, getLocation().offset + start, getLocation().line, getLocation().linePos + start);
    }
}