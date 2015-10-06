package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Rule;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import com.github.tartakynov.cartoshka.tree.entities.Value;
import com.github.tartakynov.cartoshka.tree.entities.literals.Numeric;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

public class ContextTest {
    private static Rule createRule(String name, int value, boolean isVariable) {
        Collection<Expression> expressions = new LinkedList<>();
        expressions.add(new Numeric(value, false));
        return new Rule(name, new Value(expressions), isVariable);
    }

    @Test
    public void testSetVariable() {
        Context context = new Context();
        Rule var1 = createRule("@a", 1, true);
        Rule var2 = createRule("@b", 2, false);
        Assert.assertEquals(var1, context.setVariable(var1));
        Assert.assertNull(context.setVariable(var2));
        Assert.assertEquals(1d, context.getVariable("@a").ev(null).toNumber(), 1e-8);
        Assert.assertNull(context.getVariable("@b"));
    }

    @Test
    public void testCreateNestedBlockContext() {
        Context context = new Context();
        Context nested = context.createNestedBlockContext();
        context.setVariable(createRule("@a", 1, true));
        nested.setVariable(createRule("@b", 2, true));
        Assert.assertEquals(1d, nested.getVariable("@a").ev(null).toNumber(), 1e-8);
        Assert.assertEquals(2d, nested.getVariable("@b").ev(null).toNumber(), 1e-8);
        Assert.assertEquals(1d, context.getVariable("@a").ev(null).toNumber(), 1e-8);
        Assert.assertNull(context.getVariable("@b"));
    }

    @Test
    public void testHasVariable() {
        Context context = new Context();
        Context nested = context.createNestedBlockContext();
        context.setVariable(createRule("@a", 1, true));

        Assert.assertTrue(context.hasVariables());
        Assert.assertFalse(nested.hasVariables());

        nested.setVariable(createRule("@b", 2, true));
        Assert.assertTrue(context.hasVariables());
        Assert.assertTrue(nested.hasVariables());
    }
}
