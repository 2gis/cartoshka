package com.github.tartakynov.cartoshka.tokenizers;

import org.junit.Assert;
import org.junit.Test;

public class TokenizerTest {
    @Test
    public void testQuotedText() {
        Tokenizer tokenizer = createTokenizer("\'abraca\' \"dabra\"");
        Assert.assertEquals(TokenType.STRING_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("abraca", tokenizer.current().getText());

        Assert.assertEquals(TokenType.STRING_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("dabra", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next.getType());
    }

    @Test
    public void testNumbers() {
        Tokenizer tokenizer = createTokenizer("3.1415926535897932384 26e-2");
        Assert.assertEquals(TokenType.NUMBER_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("3.1415926535897932384", tokenizer.current().getText());

        Assert.assertEquals(TokenType.NUMBER_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("26e-2", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next.getType());
    }

    @Test
    public void testKeywords() {
        Tokenizer tokenizer = createTokenizer("true false null Map zoom");
        Assert.assertEquals(TokenType.TRUE_LITERAL, tokenizer.next().getType());
        Assert.assertEquals(TokenType.FALSE_LITERAL, tokenizer.next().getType());
        Assert.assertEquals(TokenType.NULL_LITERAL, tokenizer.next().getType());
        Assert.assertEquals(TokenType.MAP_KEYWORD, tokenizer.next().getType());
        Assert.assertEquals(TokenType.ZOOM_KEYWORD, tokenizer.next().getType());

        Assert.assertEquals(TokenType.EOS, tokenizer.next.getType());
    }

    @Test
    public void testHashNames() {
        Tokenizer tokenizer = createTokenizer("#first-1 #second-2");
        Assert.assertEquals(TokenType.HASHNAME, tokenizer.next().getType());
        Assert.assertEquals("first-1", tokenizer.current().getText());

        Assert.assertEquals(TokenType.HASHNAME, tokenizer.next().getType());
        Assert.assertEquals("second-2", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next.getType());
    }

    @Test
    public void testVariables() {
        Tokenizer tokenizer = createTokenizer("@first-1 @second-2");
        Assert.assertEquals(TokenType.VARIABLE, tokenizer.next().getType());
        Assert.assertEquals("first-1", tokenizer.current().getText());

        Assert.assertEquals(TokenType.VARIABLE, tokenizer.next().getType());
        Assert.assertEquals("second-2", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next.getType());
    }

    @Test
    public void testSkippingWhiteSpaces() {
        Tokenizer tokenizer = createTokenizer("     /* multiline\n" +
                "comment\r\n" +
                "*/ // sinle line comment\n" +
                "<!-- HTML comment\n" +
                "multiline --> 1");
        Assert.assertEquals(TokenType.NUMBER_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("1", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next.getType());
    }

    @Test
    public void testCharacters() {
        Tokenizer tokenizer = createTokenizer("+-*/% >= <= > < =:;,");
        Assert.assertEquals(TokenType.ADD, tokenizer.next().getType());
        Assert.assertEquals(TokenType.SUB, tokenizer.next().getType());
        Assert.assertEquals(TokenType.MUL, tokenizer.next().getType());
        Assert.assertEquals(TokenType.DIV, tokenizer.next().getType());
        Assert.assertEquals(TokenType.MOD, tokenizer.next().getType());
        Assert.assertEquals(TokenType.GTE, tokenizer.next().getType());
        Assert.assertEquals(TokenType.LTE, tokenizer.next().getType());
        Assert.assertEquals(TokenType.GT, tokenizer.next().getType());
        Assert.assertEquals(TokenType.LT, tokenizer.next().getType());
        Assert.assertEquals(TokenType.EQ, tokenizer.next().getType());
        Assert.assertEquals(TokenType.COLON, tokenizer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, tokenizer.next().getType());
        Assert.assertEquals(TokenType.COMMA, tokenizer.next().getType());

        Assert.assertEquals(TokenType.EOS, tokenizer.next.getType());
    }

    @Test
    public void testEmptySource() {
        Tokenizer tokenizer = createTokenizer("");
        Assert.assertEquals(TokenType.EOS, tokenizer.next.getType());
    }

    private static Tokenizer createTokenizer(final String str) {
        return new Tokenizer() {
            private char[] source = str.toCharArray();
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
