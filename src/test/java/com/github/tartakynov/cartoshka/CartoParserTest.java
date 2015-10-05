package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.tree.Node;
import com.github.tartakynov.cartoshka.tree.Rule;
import com.github.tartakynov.cartoshka.tree.entities.Literal;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class CartoParserTest {
    @Test
    public void test() {
        ClassLoader loader = getClass().getClassLoader();
        List<Node> x = CartoParser.parse(new StringReader("@a: 1; @x: 1 + @a,2,rgb(50%,60%,70%);"));
        //List<Node> x = CartoParser.parse(new InputStreamReader(loader.getResourceAsStream("roads.mss")));
        Literal y = ((Rule) x.get(1)).getValue().ev();
        System.out.println(y.toString());
    }
}