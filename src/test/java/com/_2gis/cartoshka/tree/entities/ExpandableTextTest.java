package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Context;
import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.tree.Rule;
import com._2gis.cartoshka.tree.entities.literals.Numeric;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ExpandableTextTest {
    private static Rule createRule(String name, int value, boolean isVariable) {
        Collection<Expression> expressions = new LinkedList<>();
        expressions.add(new Numeric(null, value, false));
        return new Rule(null, name, new Value(null, expressions), isVariable);
    }

    private static Feature featureMock() {
        return new Feature() {
            @Override
            public String getLayer() {
                return "layer";
            }

            @Override
            public Set<String> getClasses() {
                return new HashSet<>();
            }

            @Override
            public Literal getField(String fieldName) {
                if (fieldName.equals("a")) {
                    return new Numeric(null, 123, false);
                } else if (fieldName.equals("Ab")) {
                    return new Numeric(null, 234, false);
                }

                return null;
            }
        };
    }

    @Test
    public void testEscapes() {
        Context context = new Context();
        Feature feature = featureMock();
        context.setVariable(createRule("@b", 1, true));

        ExpandableText et = new ExpandableText(null, context, "\\\\ \\[a\\] \\@{b} [a] @{b}", false);
        Assert.assertEquals("\\ [a] @{b} 123 1", et.ev(feature).toString());
    }

    @Test
    public void testFieldsInterpolation() {
        Feature feature = featureMock();
        ExpandableText et = new ExpandableText(null, null, "[a] [a] [Ab] [a] [Ab]", false);
        Assert.assertEquals("123 123 234 123 234", et.ev(feature).toString());
    }

    @Test
    public void testVariablesInterpolation() {
        Context context = new Context();
        context.setVariable(createRule("@a", 1, true));
        context.setVariable(createRule("@b", 2, true));
        ExpandableText et = new ExpandableText(null, context, "@{a} @{a} @{b} @{a} @{b}", false);
        Assert.assertEquals("1 1 2 1 2", et.ev(null).toString());
    }
}