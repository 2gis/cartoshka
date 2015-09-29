package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Node;
import com.github.tartakynov.cartoshka.tree.Rule;
import com.github.tartakynov.cartoshka.tree.entities.Expression;
import org.junit.Test;

import java.io.StringReader;
import java.util.Collection;

public class CartoParserTest {
    @Test
    public void test() {
        ClassLoader loader = getClass().getClassLoader();
        Collection<Node> x = CartoParser.parse(new StringReader("@X: alpha(fadeout(rgb(49,129,116), 99%));"));//new InputStreamReader(loader.getResourceAsStream("roads.mss")));
        Collection<Expression> expressions = ((Rule) (x.iterator().next())).getValue().getExpressions();
        Object y = expressions.iterator().next().ev();
        System.out.println(y.toString());
    }
}