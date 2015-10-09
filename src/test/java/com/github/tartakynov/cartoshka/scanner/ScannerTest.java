package com.github.tartakynov.cartoshka.scanner;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class ScannerTest {
    private static Scanner createTokenizer(final String str) {
        Scanner scanner = new Scanner();
        scanner.initialize(new Source("test", new StringReader(str)));
        return scanner;
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
    public void testIdentifiers() {
        Scanner scanner = createTokenizer("a/b /b");
        Assert.assertEquals(TokenType.IDENTIFIER, scanner.next().getType());
        Assert.assertEquals("a/b", scanner.current().getText());
        Assert.assertEquals(TokenType.DIV, scanner.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, scanner.next().getType());
        Assert.assertEquals("b", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testHashes() {
        Scanner scanner = createTokenizer("#first-1 #second-2 #012 #012345 #707d05");
        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals("first-1", scanner.current().getText());

        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals("second-2", scanner.current().getText());

        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals("012", scanner.current().getText());

        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals("012345", scanner.current().getText());

        Assert.assertEquals(TokenType.HASH, scanner.next().getType());
        Assert.assertEquals("707d05", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testVariables() {
        Scanner scanner = createTokenizer("@first-1 @second-2");
        Assert.assertEquals(TokenType.VARIABLE, scanner.next().getType());
        Assert.assertEquals("@first-1", scanner.current().getText());

        Assert.assertEquals(TokenType.VARIABLE, scanner.next().getType());
        Assert.assertEquals("@second-2", scanner.current().getText());

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
        Scanner scanner = createTokenizer("+-*/% >= <= > < != =:;,. 1.2");
        Assert.assertEquals(TokenType.ADD, scanner.next().getType());
        Assert.assertEquals(TokenType.SUB, scanner.next().getType());
        Assert.assertEquals(TokenType.MUL, scanner.next().getType());
        Assert.assertEquals(TokenType.DIV, scanner.next().getType());
        Assert.assertEquals(TokenType.MOD, scanner.next().getType());
        Assert.assertEquals(TokenType.GTE, scanner.next().getType());
        Assert.assertEquals(TokenType.LTE, scanner.next().getType());
        Assert.assertEquals(TokenType.GT, scanner.next().getType());
        Assert.assertEquals(TokenType.LT, scanner.next().getType());
        Assert.assertEquals(TokenType.NE, scanner.next().getType());
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
    public void testAttachment() {
        Scanner scanner = createTokenizer("::a/b ::abcd ::-a/b");
        Assert.assertEquals(TokenType.ATTACHMENT, scanner.next().getType());
        Assert.assertEquals("a/b", scanner.current().getText());

        Assert.assertEquals(TokenType.ATTACHMENT, scanner.next().getType());
        Assert.assertEquals("abcd", scanner.current().getText());

        Assert.assertEquals(TokenType.ATTACHMENT, scanner.next().getType());
        Assert.assertEquals("-a/b", scanner.current().getText());

        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }

    @Test
    public void testEmptySource() {
        Scanner scanner = createTokenizer("");
        Assert.assertEquals(TokenType.EOS, scanner.next().getType());
    }
}