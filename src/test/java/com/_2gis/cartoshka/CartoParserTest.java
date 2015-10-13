package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.Node;
import com._2gis.cartoshka.tree.entities.Literal;
import com._2gis.cartoshka.tree.entities.literals.Numeric;
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
                return new Numeric(null, 100500, false);
            }
        };
    }

    @Test
    public void test() {

        CartoParser parser = new CartoParser();
        parser.addSource("test", new StringReader("a: 10%, 10m, 10cm, 10in, 10mm, 10pt, 10pc, 10px;"));
        List<Node> nodes = parser.parse();
        nodes.toArray();
//        ClassLoader cl = this.getClass().getClassLoader();
//        parser.addSource("roads.mss", new InputStreamReader(cl.getResourceAsStream("roads.mss")));
//        parser.addSource("style.mss", new InputStreamReader(cl.getResourceAsStream("style.mss")));
//        parser.addSource("test", new StringReader("@x: [field]; y: @x + 1; z: blur();"));
//        Queue<Node> queue = new LinkedBlockingQueue<>();
//        queue.addAll(parser.parse());
//        while (!queue.isEmpty()) {
//            Node node = queue.poll();
//            if (node instanceof Rule) {
//                Rule rule = (Rule) node;
//                Literal value = rule.getValue().ev(featureMock());
//                System.out.println(
//                        String.format("%s: %s;",
//                                rule.isDefaultInstance() ? rule.getName() : rule.getInstance() + "/" + rule.getName(),
//                                value)
//                );
//            } else if (node instanceof Ruleset) {
//                Ruleset ruleset = (Ruleset) node;
//                queue.addAll(ruleset.getRules());
//            }
//        }
    }
}