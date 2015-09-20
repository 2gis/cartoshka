package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Node;
import org.junit.Test;

import java.io.StringReader;
import java.util.Collection;

public class CartoParserTest {
    @Test
    public void test() {
        Collection<Node> x = CartoParser.parse(new StringReader(".roads-casing, .bridges-casing, .tunnels-casing::casing, [zoom = 9][feature = 'highway_secondary'] {\n" +
                "x: 1;\n" +
                "}"));
        x.toArray();
    }
}