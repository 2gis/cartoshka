package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.Context;
import com.github.tartakynov.cartoshka.Feature;
import com.github.tartakynov.cartoshka.tree.entities.literals.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableText extends Expression {
    private final HashMap<Field, String> fields = new HashMap<>();

    private final HashMap<Variable, String> variables = new HashMap<>();

    private final Context context;

    private final String value;

    public ExpandableText(Context context, String value) {
        this.context = context;
        this.value = initialize(value);
    }

    private String initialize(String value) {
        int start = 0;
        int length = value.length();
        StringBuilder sb = new StringBuilder();
        while (start < length) {
            switch (value.charAt(start)) {
                case '@':
                    if (value.charAt(start + 1) == '{') {
                        int end = value.indexOf('}', start + 2);
                        if (end > start + 2) {
                            Variable var = new Variable(context, '@' + value.substring(start + 2, end));
                            variables.put(var, "@\\{" + value.substring(start + 2, end) + "\\}");
                            sb.append(value.substring(start, end + 1));
                            start = end + 1;
                        }
                    }

                    break;

                case '[':
                    int end = value.indexOf(']', start + 1);
                    if (end > start + 1) {
                        Field field = new Field(value.substring(start + 1, end));
                        fields.put(field, '\\' + value.substring(start, end) + "\\]");
                        sb.append(value.substring(start, end + 1));
                        start = end + 1;
                    }

                    break;

                case '\\':
                    start += 1;
            }

            if (start < length) {
                sb.append(value.charAt(start));
            }

            start++;
        }

        return sb.toString();
    }

    @Override
    public Literal ev(Feature feature) {
        // expand variables
        String result = value;
        for (java.util.Map.Entry<Variable, String> entry : variables.entrySet()) {
            String pattern = entry.getValue();
            Variable var = entry.getKey();
            result = result.replaceAll(pattern, var.ev(feature).toString());
        }

        // expand fields
        for (java.util.Map.Entry<Field, String> entry : fields.entrySet()) {
            String pattern = entry.getValue();
            Field field = entry.getKey();
            result = result.replaceAll(pattern, field.ev(feature).toString());
        }

        return new Text(result);
    }

    @Override
    public boolean isDynamic() {
        return !fields.isEmpty() || hasDynamicExpression(new ArrayList<Expression>(variables.keySet()));
    }
}