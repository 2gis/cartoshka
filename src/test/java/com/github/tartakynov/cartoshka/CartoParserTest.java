package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Node;
import com.github.tartakynov.cartoshka.tree.Rule;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Numeric;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartoParserTest {
    private static Feature featureMock() {
        return new Feature() {
            @Override
            public String getLayer() {
                return "testLayer";
            }

            @Override
            public Set<String> getClasses() {
                return new HashSet<>();
            }

            @Override
            public Literal getField(String fieldName) {
                if (fieldName.equals("test")) {
                    return new Numeric(100500, false);
                }

                return null;
            }
        };
    }

    @Test
    public void test() {
        List<Node> ast = CartoParser.parse(new StringReader("@a: [test]; x: 1 + @a,2,rgb(50%,60%,70%);"));
        for (Node node : ast) {
            if (node instanceof Rule) {
                Rule rule = (Rule) node;
                Literal value = rule.getValue().ev(featureMock());
                System.out.println(String.format("%s: %s;", rule.getName(), value));
            }
        }
    }
}