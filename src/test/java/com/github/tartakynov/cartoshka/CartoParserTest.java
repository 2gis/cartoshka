package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Node;
import com.github.tartakynov.cartoshka.tree.Rule;
import com.github.tartakynov.cartoshka.tree.Ruleset;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import com.github.tartakynov.cartoshka.tree.entities.literals.Numeric;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

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
                return new Numeric(100500, false);
            }
        };
    }

    @Test
    public void test() {
        CartoParser parser = new CartoParser();
        ClassLoader cl = this.getClass().getClassLoader();
//        parser.addSource(new InputStreamReader(cl.getResourceAsStream("roads.mss")));
//        parser.addSource(new InputStreamReader(cl.getResourceAsStream("style.mss")));
        parser.addSource(new StringReader("a: mix(#ff0000, #0000ff, 50%); b: mix(rgba(100,0,0,1.0), rgba(0,100,0,0.5), 50%); c: agg-stack-blur(1, 2);"));
        Queue<Node> queue = new LinkedBlockingQueue<>();
        queue.addAll(parser.parse());
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node instanceof Rule) {
                Rule rule = (Rule) node;
                Literal value = rule.getValue().ev(featureMock());
                System.out.println(
                        String.format("%s: %s;",
                                rule.isDefaultInstance() ? rule.getName() : rule.getInstance() + "/" + rule.getName(),
                                value)
                );
            } else if (node instanceof Ruleset) {
                Ruleset ruleset = (Ruleset) node;
                queue.addAll(ruleset.getRules());
            }
        }
    }
}