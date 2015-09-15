package com.github.tartakynov.cartoshka;

import com.metaweb.lessen.Utilities;
import com.metaweb.lessen.tokenizers.Tokenizer;
import org.junit.Test;

import java.io.StringReader;

public class CartoParserTest {
    @Test
    public void test() {
        Tokenizer t = Utilities.open(new StringReader(".access::fill *;\n"));
        while (true) {
            if (t.getToken() == null) {
                break;
            }

            /* */
            System.out.println(t.getToken());
            t.next();
        }
    }

    @Test
    public void x() {
    }
}
