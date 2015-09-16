package com.github.tartakynov.cartoshka.tokenizers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenizerTest {
    Tokenizer tokenizer = null;

    @Test
    public void testTokenizer() {
        Assert.assertEquals(tokenizer.next().getType(), TokenType.VARIABLE);
        Assert.assertEquals(tokenizer.current().getText(), "hello");

        Assert.assertEquals(tokenizer.next().getType(), TokenType.HASHNAME);
        Assert.assertEquals(tokenizer.current().getText(), "world");

        Assert.assertEquals(tokenizer.next().getType(), TokenType.NUMBER_LITERAL);
        Assert.assertEquals(tokenizer.current().getText(), "1.2");

        Assert.assertEquals(tokenizer.next().getType(), TokenType.NUMBER_LITERAL);
        Assert.assertEquals(tokenizer.current().getText(), "1.2e+5");

        Assert.assertEquals(tokenizer.next().getType(), TokenType.STRING_LITERAL);
        Assert.assertEquals(tokenizer.current().getText(), "quoted");

        Assert.assertEquals(tokenizer.next().getType(), TokenType.IDENTIFIER);
        Assert.assertEquals(tokenizer.current().getText(), "Id3nti-er");

        Assert.assertEquals(tokenizer.next().getType(), TokenType.MUL);
        Assert.assertEquals(tokenizer.current().getText(), "");

        Assert.assertEquals(tokenizer.next().getType(), TokenType.IDENTIFIER);
        Assert.assertEquals(tokenizer.current().getText(), "x");

        Assert.assertEquals(tokenizer.next().getType(), TokenType.IDENTIFIER);
        Assert.assertEquals(tokenizer.current().getText(), "zoom-x");
    }

    @Before
    public void init() {
        tokenizer = new Tokenizer() {
            private char[] source = ("@hello #world 1.2 1.2e+5 'quoted'\n" +
                    "/* comment */ <!-- html comment --> Id3nti-er*" +
                    "\n// line comment\nx zoom-x").toCharArray();
            private int position = -1;

            public Tokenizer init() {
                initialize();
                return this;
            }

            @Override
            protected boolean advance() {
                position++;
                if (position >= source.length) {
                    return false;
                }

                this.c0_ = source[position];
                return true;
            }

            @Override
            protected int getCurrentPosition() {
                return position;
            }

            @Override
            protected boolean isEOS() {
                return position >= source.length;
            }
        }.init();
    }
}
