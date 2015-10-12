package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Context;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.tree.entities.literals.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ExpandableText extends Expression {
    private final Context context;

    private final List<Expression> expressions;

    private final boolean isURL;

    public ExpandableText(Location location, Context context, String value, boolean isURL) {
        super(location);
        this.context = context;
        this.isURL = isURL;
        this.expressions = parse(value);
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
                                expressions.add(new Text(getLocation(), sb.toString(), false, false));
                                sb.setLength(0);
                            }

                            expressions.add(new Variable(getSubLocation(start), context, "@" + name));
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
                            expressions.add(new Text(getLocation(), sb.toString(), false, false));
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
            expressions.add(new Text(null, sb.toString(), false, false));
        }

        return expressions;
    }

    @Override
    public String toString() {
        return collectionToString(expressions, "");
    }

    @Override
    public Literal ev(Feature feature) {
        StringBuilder builder = new StringBuilder();
        for (Expression expression : expressions) {
            builder.append(expression.ev(feature).toString());
        }

        return new Text(getLocation(), builder.toString(), isURL, false);
    }

    public boolean isPlain() {
        return expressions.size() == 1 && (expressions.get(0).isLiteral());
    }

    @Override
    public boolean isDynamic() {
        return hasDynamicExpression(expressions);
    }

    @Override
    public void fold() {
        Stack<Expression> stack = new Stack<>();
        for (Expression ex : expressions) {
            ex.fold();
            if (!stack.isEmpty() && !stack.peek().isDynamic() && !ex.isDynamic()) {
                Expression nex = stack.pop();
                stack.push(new Text(null, nex.ev(null).toString() + ex.ev(null).toString(), false, false));
            } else {
                stack.push(ex);
            }
        }

        expressions.clear();
        expressions.addAll(stack);
    }

    private Location getSubLocation(int start) {
        return getLocation() == null
                ? null
                : new Location(getLocation().name, getLocation().offset + start, getLocation().line, getLocation().linePos + start);
    }
}