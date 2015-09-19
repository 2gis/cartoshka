package com.github.tartakynov.cartoshka.tokenizers;

import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;

public class TokenizerTest {
    private static Tokenizer createTokenizer(final String str) {
        return new Tokenizer() {
            private final char[] source = str.toCharArray();
            private final Stack<Character> stack = new Stack<Character>();
            private int position = -1;

            public Tokenizer init() {
                initialize();
                return this;
            }

            @Override
            protected boolean advance() {
                position++;
                if (stack.isEmpty()) {
                    if (position >= source.length) {
                        return false;
                    }

                    this.c0_ = source[position];
                } else {
                    this.c0_ = stack.pop();
                }

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

            @Override
            protected void push(char c) {
                stack.push(c);
                position--;
            }
        }.init();
    }

    @Test
    public void testQuotedText() {
        Tokenizer tokenizer = createTokenizer("\'abraca\' \"dabra\"");
        Assert.assertEquals(TokenType.STRING_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("abraca", tokenizer.current().getText());

        Assert.assertEquals(TokenType.STRING_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("dabra", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
    }

    @Test
    public void testNumbers() {
        Tokenizer tokenizer = createTokenizer("3.1415926535897932384 26e-2");
        Assert.assertEquals(TokenType.NUMBER_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("3.1415926535897932384", tokenizer.current().getText());

        Assert.assertEquals(TokenType.NUMBER_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("26e-2", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
    }

    @Test
    public void testKeywords() {
        Tokenizer tokenizer = createTokenizer("true false null Map zoom");
        Assert.assertEquals(TokenType.TRUE_LITERAL, tokenizer.next().getType());
        Assert.assertEquals(TokenType.FALSE_LITERAL, tokenizer.next().getType());
        Assert.assertEquals(TokenType.NULL_LITERAL, tokenizer.next().getType());
        Assert.assertEquals(TokenType.MAP_KEYWORD, tokenizer.next().getType());
        Assert.assertEquals(TokenType.ZOOM_KEYWORD, tokenizer.next().getType());

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
    }

    @Test
    public void testHashes() {
        Tokenizer tokenizer = createTokenizer("#first-1 #second-2 #012 #012345");
        Assert.assertEquals(TokenType.HASH, tokenizer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, tokenizer.next().getType());
        Assert.assertEquals("first-1", tokenizer.current().getText());

        Assert.assertEquals(TokenType.HASH, tokenizer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, tokenizer.next().getType());
        Assert.assertEquals("second-2", tokenizer.current().getText());

        Assert.assertEquals(TokenType.HASH, tokenizer.next().getType());
        Assert.assertEquals(TokenType.NUMBER_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("012", tokenizer.current().getText());

        Assert.assertEquals(TokenType.HASH, tokenizer.next().getType());
        Assert.assertEquals(TokenType.NUMBER_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("012345", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
    }

    @Test
    public void testVariables() {
        Tokenizer tokenizer = createTokenizer("@first-1 @second-2");
        Assert.assertEquals(TokenType.VARIABLE, tokenizer.next().getType());
        Assert.assertEquals("first-1", tokenizer.current().getText());

        Assert.assertEquals(TokenType.VARIABLE, tokenizer.next().getType());
        Assert.assertEquals("second-2", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
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

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
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

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
    }

    @Test
    public void testDimensions() {
        Tokenizer tokenizer = createTokenizer("3.1415926535897932384pt 26e-2% 2e+2cm 1.23xy");
        Assert.assertEquals(TokenType.DIMENSION_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("3.1415926535897932384pt", tokenizer.current().getText());

        Assert.assertEquals(TokenType.DIMENSION_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("26e-2%", tokenizer.current().getText());

        Assert.assertEquals(TokenType.DIMENSION_LITERAL, tokenizer.next().getType());
        Assert.assertEquals("2e+2cm", tokenizer.current().getText());

        Assert.assertEquals(TokenType.ILLEGAL, tokenizer.next().getType());
        Assert.assertEquals("1.23xy", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
    }

    @Test
    public void testUrl() {
        Tokenizer tokenizer = createTokenizer("url(\"/myfolder/img.png\") url(/myfolder/img.png) url('/myfolder/img.png')");
        Assert.assertEquals(TokenType.URL, tokenizer.next().getType());
        Assert.assertEquals("/myfolder/img.png", tokenizer.current().getText());

        Assert.assertEquals(TokenType.URL, tokenizer.next().getType());
        Assert.assertEquals("/myfolder/img.png", tokenizer.current().getText());

        Assert.assertEquals(TokenType.URL, tokenizer.next().getType());
        Assert.assertEquals("/myfolder/img.png", tokenizer.current().getText());

        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());

    }

    @Test
    public void testEmptySource() {
        Tokenizer tokenizer = createTokenizer("");
        Assert.assertEquals(TokenType.EOS, tokenizer.next().getType());
    }
}
