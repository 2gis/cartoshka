package com.github.tartakynov.cartoshka.scanners;

import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;

public class ScannerTest {
    private static Scanner createTokenizer(final String str) {
        return new Scanner() {
            private final char[] source = str.toCharArray();
            private final Stack<Character> stack = new Stack<>();
            private int position = -1;

            public Scanner init() {
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
        Scanner scanner = createTokenizer("\'abraca\' \"dabra\"");
        Assert.assertEquals(TokenType.STRING_LITERAL, scanner.next().getType());
        Assert.assertEquals("abraca", scanner.current().getText());

        Assert.assertEquals(TokenType.STRING_LITERAL, scanner.next().getType());
        Assert.assertEquals("dabra", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testNumbers() {
        Scanner scanner = createTokenizer("3.1415926535897932384 26e-2");
        Assert.assertEquals(TokenType.NUMBER_LITERAL, scanner.next().getType());
        Assert.assertEquals("3.1415926535897932384", scanner.current().getText());

        Assert.assertEquals(TokenType.NUMBER_LITERAL, scanner.next().getType());
        Assert.assertEquals("26e-2", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testKeywords() {
        Scanner scanner = createTokenizer("true false Map zoom");
        Assert.assertEquals(TokenType.TRUE_LITERAL, scanner.next().getType());
        Assert.assertEquals(TokenType.FALSE_LITERAL, scanner.next().getType());
        Assert.assertEquals(TokenType.MAP_KEYWORD, scanner.next().getType());
        Assert.assertEquals(TokenType.ZOOM_KEYWORD, scanner.next().getType());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testHashes() {
        Scanner scanner = createTokenizer("#first-1 #second-2 #012 #012345");
        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, scanner.next().getType());
        Assert.assertEquals("first-1", scanner.current().getText());

        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, scanner.next().getType());
        Assert.assertEquals("second-2", scanner.current().getText());

        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals(TokenType.NUMBER_LITERAL, scanner.next().getType());
        Assert.assertEquals("012", scanner.current().getText());

        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals(TokenType.NUMBER_LITERAL, scanner.next().getType());
        Assert.assertEquals("012345", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testVariables() {
        Scanner scanner = createTokenizer("@first-1 @second-2");
        Assert.assertEquals(TokenType.VARIABLE, scanner.next().getType());
        Assert.assertEquals("first-1", scanner.current().getText());

        Assert.assertEquals(TokenType.VARIABLE, scanner.next().getType());
        Assert.assertEquals("second-2", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testSkippingWhiteSpaces() {
        Scanner scanner = createTokenizer("     /* multiline\n" +
                "comment\r\n" +
                "*/ // sinle line comment\n" +
                "<!-- HTML comment\n" +
                "multiline --> 1");
        Assert.assertEquals(TokenType.NUMBER_LITERAL, scanner.next().getType());
        Assert.assertEquals("1", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testCharacters() {
        Scanner scanner = createTokenizer("+-*/% >= <= > < =:;,. 1.2");
        Assert.assertEquals(TokenType.ADD, scanner.next().getType());
        Assert.assertEquals(TokenType.SUB, scanner.next().getType());
        Assert.assertEquals(TokenType.MUL, scanner.next().getType());
        Assert.assertEquals(TokenType.DIV, scanner.next().getType());
        Assert.assertEquals(TokenType.MOD, scanner.next().getType());
        Assert.assertEquals(TokenType.GTE, scanner.next().getType());
        Assert.assertEquals(TokenType.LTE, scanner.next().getType());
        Assert.assertEquals(TokenType.GT, scanner.next().getType());
        Assert.assertEquals(TokenType.LT, scanner.next().getType());
        Assert.assertEquals(TokenType.EQ, scanner.next().getType());
        Assert.assertEquals(TokenType.COLON, scanner.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, scanner.next().getType());
        Assert.assertEquals(TokenType.COMMA, scanner.next().getType());
        Assert.assertEquals(TokenType.PERIOD, scanner.next().getType());
        Assert.assertEquals(TokenType.NUMBER_LITERAL, scanner.next().getType());
        Assert.assertEquals("1.2", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testDimensions() {
        Scanner scanner = createTokenizer("3.1415926535897932384pt 26e-2% 2e+2cm 1.23xy");
        Assert.assertEquals(TokenType.DIMENSION_LITERAL, scanner.next().getType());
        Assert.assertEquals("3.1415926535897932384pt", scanner.current().getText());

        Assert.assertEquals(TokenType.DIMENSION_LITERAL, scanner.next().getType());
        Assert.assertEquals("26e-2%", scanner.current().getText());

        Assert.assertEquals(TokenType.DIMENSION_LITERAL, scanner.next().getType());
        Assert.assertEquals("2e+2cm", scanner.current().getText());

        Assert.assertEquals(TokenType.ILLEGAL, scanner.next().getType());
        Assert.assertEquals("1.23xy", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testUrl() {
        Scanner scanner = createTokenizer("url(\"/myfolder/img.png\") url(/myfolder/img.png) url('/myfolder/img.png')");
        Assert.assertEquals(TokenType.URL, scanner.next().getType());
        Assert.assertEquals("/myfolder/img.png", scanner.current().getText());

        Assert.assertEquals(TokenType.URL, scanner.next().getType());
        Assert.assertEquals("/myfolder/img.png", scanner.current().getText());

        Assert.assertEquals(TokenType.URL, scanner.next().getType());
        Assert.assertEquals("/myfolder/img.png", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testEmptySource() {
        Scanner scanner = createTokenizer("");
        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }
}
