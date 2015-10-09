package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Context;
import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.tree.entities.literals.Text;

import java.util.HashMap;

public class ExpandableText extends Expression {
    private final HashMap<String, Field> fields = new HashMap<>();

    private final HashMap<String, Variable> variables = new HashMap<>();

    private final Context context;

    private final String value;

    private final boolean isURL;

    public ExpandableText(Context context, String value) {
        this.context = context;
        this.value = initialize(value);
        this.isURL = false;
    }

    public ExpandableText(Context context, String value, boolean isURL) {
        this.context = context;
        this.value = initialize(value);
        this.isURL = isURL;
    }

    private String initialize(String value) {
        int start = 0;
        int length = value.length();
        StringBuilder sb = new StringBuilder();
        while (start < length) {
            switch (value.charAt(start)) {
                case '\\':
                    start++;
                    break;

                case '@':
                    if (value.charAt(start + 1) == '{') {
                        int end = value.indexOf('}', start + 3);
                        if (start < end) {
                            String name = value.substring(start + 2, end);
                            String pattern = ":@\\{" + name + "\\}";
                            variables.put(pattern, new Variable(context, '@' + name));
                            sb.append(':');
                        }
                    }

                    break;

                case '[':
                    int end = value.indexOf(']', start + 1);
                    if (start < end) {
                        String name = value.substring(start + 1, end);
                        String pattern = String.format(":\\[%s\\]", name);
                        fields.put(pattern, new Field(name));
                        sb.append(':');
                    }

                    break;
            }

            if (start < length) {
                sb.append(value.charAt(start));
                start++;
            }
        }

        return sb.toString();
    }

    @Override
    public Literal ev(Feature feature) {
        String result = value;
        for (java.util.Map.Entry<String, Variable> entry : variables.entrySet()) {
            String pattern = entry.getKey();
            Variable var = entry.getValue();
            result = result.replaceAll(pattern, var.ev(feature).toString());
        }

        for (java.util.Map.Entry<String, Field> entry : fields.entrySet()) {
            String pattern = entry.getKey();
            Field field = entry.getValue();
            result = result.replaceAll(pattern, field.ev(feature).toString());
        }

        return new Text(getLocation(), result, isURL, false);
    }

    public boolean isPlain() {
        return fields.isEmpty() && variables.isEmpty();
    }

    @Override
    public boolean isDynamic() {
        return !fields.isEmpty() || hasDynamicExpression(variables.values());
    }

    @Override
    public void fold() {
        fold(variables.values());
    }
}