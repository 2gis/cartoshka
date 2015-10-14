package com._2gis.cartoshka;

import com._2gis.cartoshka.tree.Style;
import com._2gis.cartoshka.tree.entities.Literal;
import com._2gis.cartoshka.tree.entities.literals.Numeric;
import com._2gis.cartoshka.visitors.ConstantFoldingVisitor;
import com._2gis.cartoshka.visitors.PrintVisitor;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashSet;
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
        ConstantFoldingVisitor ev = new ConstantFoldingVisitor();
        PrintVisitor pv = new PrintVisitor(4);
        parser.addSource("test", new StringReader("    @some-color: #abc;\n" +
                "    .class-1, .class-2, .class-3 {\n" +
                "      #id1::x/attachment-1, #id2::attachment-2, ::attachment-3 {\n" +
                "        #x[zoom >= 10][f1 = 'v1'][f2 > 10] {\n" +
                "          line-color: @some-color;\n" +
                "          line-width: 2.2;\n" +
                "          line-opacity: 0.4;\n" +
                "          a/line-join: miter;\n" +
                "          b/line-join: round;\n" +
                "          Map {\n" +
                "            just: 'x @{some-color}';\n" +
                "            a: [asd];\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }"));

        Style style = parser.parse();
        // style.accept(ev, null);

        System.out.println(style.accept(pv, null));
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