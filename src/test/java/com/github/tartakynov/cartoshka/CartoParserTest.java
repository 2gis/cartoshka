package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Node;
import org.junit.Test;

import java.io.InputStreamReader;
import java.util.Collection;

public class CartoParserTest {
    @Test
    public void test() {
        ClassLoader loader = getClass().getClassLoader();
        Collection<Node> x = CartoParser.parse(new InputStreamReader(loader.getResourceAsStream("roads.mss")));
        x.toArray();
    }
}