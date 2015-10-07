package com.github.tartakynov.cartoshka.scanners;

import com.github.tartakynov.cartoshka.exceptions.UnexpectedCharException;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Scanner {
    protected static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    protected static final Set<String> DIMENSION_UNITS = new HashSet<>(Arrays.asList("m", "cm", "in", "mm", "pt", "pc", "px", "%"));

    static {
        for (TokenType type : TokenType.values()) {
            if (type.getStr() != null && !type.getStr().isEmpty()) {
                String str = type.getStr();
                if (Character.isJavaIdentifierStart(str.charAt(0))) {
                    KEYWORDS.put(str, type);
                }
            }
        }
    }

    private final StringBuilder literal = new StringBuilder();
    protected char c0_;
    private Reader source;
    private Token next;
    private Token current;
    private int position;
    private boolean eos;

    private static boolean isLineTerminator(char c) {
        return c == '\n' || c == '\r';
    }

    protected void initialize(Reader source) {
        this.position = -1;
        this.eos = false;
        this.current = null;
        this.source = source;
        this.literal.setLength(0);
        advance();
        skipWhiteSpace();
        scan();
    }

    protected Token peek() {
        return next;
    }

    protected Token next() {
        current = next;
        scan();

        return current;
    }

    protected Token current() {
        return current;
    }

    private void expect(char expected) {
        if (expected != c0_) {
            throw new UnexpectedCharException(c0_, getCurrentPosition());
        }
    }

    protected boolean advance() {
        try {
            int c = this.source.read();
            if (c < 0) {
                this.eos = true;
                return false;
            }

            this.c0_ = (char) c;
            this.position++;
            return true;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected int getCurrentPosition() {
        return this.position;
    }

    protected boolean isEOS() {
        return this.eos;
    }

    protected void scan() {
        int posStart;
        int posEnd;
        TokenType token;
        literal.setLength(0);

        do {
            posStart = getCurrentPosition();
            if (isEOS()) {
                token = TokenType.EOS;
                break;
            }

            switch (c0_) {
                case ' ':
                case '\t':
                case '\n':
                    token = select(TokenType.WHITESPACE);
                    break;

                case '"':
                    token = scanString('"', '"');
                    break;

                case '\'':
                    token = scanString('\'', '\'');
                    break;

                case '<':
                    // < <= <!--
                    advance();
                    if (c0_ == '=') {
                        token = select(TokenType.LTE);
                    } else if (c0_ == '!') {
                        token = scanHtmlComment();
                    } else {
                        token = TokenType.LT;
                    }
                    break;

                case '>':
                    // > >=
                    advance();
                    if (c0_ == '=') {
                        token = select(TokenType.GTE);
                    } else {
                        token = TokenType.GT;
                    }
                    break;

                case '=':
                    // =
                    token = select(TokenType.EQ);
                    break;

                case '!':
                    advance();
                    if (c0_ == '=') {
                        token = select(TokenType.NE);
                    } else {
                        token = TokenType.ILLEGAL;
                    }
                    break;

                case '+':
                    // +
                    token = select(TokenType.ADD);
                    break;

                case '-':
                    // -
                    token = select(TokenType.SUB);
                    break;

                case '*':
                    // *
                    token = select(TokenType.MUL);
                    break;

                case '%':
                    // %
                    token = select(TokenType.MOD);
                    break;

                case '/':
                    // /  // /*
                    advance();
                    if (c0_ == '/') {
                        token = skipSingleLineComment();
                    } else if (c0_ == '*') {
                        token = skipMultiLineComment();
                    } else {
                        token = TokenType.DIV;
                    }
                    break;

                case ':':
                    advance();
                    if (c0_ == ':') {
                        token = scanAttachment();
                    } else {
                        token = TokenType.COLON;
                    }
                    break;

                case ';':
                    token = select(TokenType.SEMICOLON);
                    break;

                case ',':
                    token = select(TokenType.COMMA);
                    break;

                case '.':
                    token = select(TokenType.PERIOD);
                    break;

                case '(':
                    token = select(TokenType.LPAREN);
                    break;

                case ')':
                    token = select(TokenType.RPAREN);
                    break;

                case '[':
                    token = select(TokenType.LBRACK);
                    break;

                case ']':
                    token = select(TokenType.RBRACK);
                    break;

                case '{':
                    token = select(TokenType.LBRACE);
                    break;

                case '}':
                    token = select(TokenType.RBRACE);
                    break;

                case '#':
                    token = scanHash();
                    break;

                case '@':
                    token = scanVariable();
                    break;

                default:
                    if (Character.isJavaIdentifierStart(c0_)) {
                        token = scanIdentifierOrKeyword();
                        if (token == TokenType.IDENTIFIER && c0_ == '(' && literal.toString().equals("url")) {
                            literal.setLength(0);
                            token = scanUrl();
                        }

                    } else if (Character.isDigit(c0_)) {
                        token = scanNumberOrDimension(false);
                    } else if (skipWhiteSpace()) {
                        token = TokenType.WHITESPACE;
                    } else {
                        token = select(TokenType.ILLEGAL);
                    }
                    break;
            }
        } while (token == TokenType.WHITESPACE);

        posEnd = getCurrentPosition();
        next = new Token(token, posStart, posEnd, literal.toString());
    }

    private TokenType skipSingleLineComment() {
        advance();
        while (!isLineTerminator(c0_)) {
            if (!advance()) {
                break;
            }
        }

        return TokenType.WHITESPACE;
    }

    private TokenType skipMultiLineComment() {
        expect('*');
        char c = c0_;
        while (advance()) {
            if (c == '*' && c0_ == '/') {
                c0_ = ' ';
                return TokenType.WHITESPACE;
            }

            c = c0_;
        }

        return TokenType.ILLEGAL;
    }

    private boolean skipWhiteSpace() {
        int skipped = 0;
        while (Character.isWhitespace(c0_)) {
            if (!advance()) {
                break;
            }

            skipped++;
        }

        return skipped > 0;
    }

    private TokenType select(TokenType type) {
        advance();
        return type;
    }

    private TokenType scanString(char leftQuote, char rightQuote) {
        expect(leftQuote);
        while (advance()) {
            if (isLineTerminator(c0_)) {
                break;
            } else if (c0_ == rightQuote) {
                advance(); // consume quote
                return TokenType.STRING_LITERAL;
            } else if (c0_ == '\\') {
                advance();
                literal.append(c0_);
            } else {
                literal.append(c0_);
            }
        }

        return TokenType.ILLEGAL;
    }

    private TokenType scanNumberOrDimension(boolean seen_period) {
        if (seen_period) {
            literal.append('.');
            scanDecimalDigits();
        } else {
            scanDecimalDigits();
            if (c0_ == '.') {
                addLiteralCharAdvance();
                scanDecimalDigits();
            }
        }

        if (isEOS()) {
            return TokenType.NUMBER_LITERAL;
        }

        // scan exponent, if any
        if (c0_ == 'e' || c0_ == 'E') {
            addLiteralCharAdvance();
            if (c0_ == '+' || c0_ == '-') {
                addLiteralCharAdvance();
            }

            if (!Character.isDigit(c0_)) {
                return TokenType.ILLEGAL;
            }

            scanDecimalDigits();
        }

        // scan dimension, if any
        if (Character.isLetter(c0_)) {
            String number = literal.toString();
            literal.setLength(0);
            while (Character.isLetter(c0_)) {
                literal.append(c0_);
                if (!advance()) {
                    break;
                }
            }

            String unit = literal.toString();
            literal.setLength(0);
            literal.append(number);
            literal.append(unit);
            if (DIMENSION_UNITS.contains(unit)) {
                return TokenType.DIMENSION_LITERAL;
            } else {
                return TokenType.ILLEGAL;
            }
        } else if (c0_ == '%') {
            literal.append(c0_);
            return select(TokenType.DIMENSION_LITERAL);
        }

        if (!isEOS() && Character.isJavaIdentifierStart(c0_)) {
            return TokenType.ILLEGAL;
        }

        return TokenType.NUMBER_LITERAL;
    }

    private void scanDecimalDigits() {
        while (Character.isDigit(c0_)) {
            literal.append(c0_);
            if (!advance()) {
                break;
            }
        }
    }

    private void scanIdentifierPart() {
        while (Character.isJavaIdentifierPart(c0_) || c0_ == '-') {
            literal.append(c0_);
            if (!advance()) {
                break;
            }
        }
    }

    private TokenType scanVariable() {
        expect('@');
        addLiteralCharAdvance(); // consume @
        if (Character.isJavaIdentifierStart(c0_)) {
            scanIdentifierPart();
            return TokenType.VARIABLE;
        }

        return TokenType.ILLEGAL;
    }

    private TokenType scanHash() {
        expect('#');
        advance(); // consume #
        if (Character.isJavaIdentifierPart(c0_)) {
            scanIdentifierPart();
            return TokenType.HASH;
        }

        return TokenType.ILLEGAL;
    }

    private TokenType scanIdentifierOrKeyword() {
        scanIdentifierPart();
        if (c0_ == '/') {
            addLiteralCharAdvance();
            scanIdentifierPart();
        }

        TokenType type = KEYWORDS.get(literal.toString());
        if (type != null) {
            return type;
        }

        return TokenType.IDENTIFIER;
    }

    private TokenType scanUrl() {
        expect('(');
        scanString('(', ')');

        // trim quotes
        if (literal.charAt(0) == '"' || literal.charAt(0) == '\'') {
            literal.deleteCharAt(literal.length() - 1);
            literal.deleteCharAt(0);
        }

        return TokenType.URL;
    }

    private TokenType scanAttachment() {
        expect(':');
        advance(); // consume :
        boolean hasLetters = false;
        do {
            if (c0_ == '/') {
                if (!hasLetters) {
                    return TokenType.ILLEGAL;
                }

                hasLetters = false;
                literal.append(c0_);
                advance();
            }

            while (Character.isLetter(c0_) || c0_ == '_' || c0_ == '-') {
                literal.append(c0_);
                hasLetters = true;
                if (!advance()) {
                    break;
                }
            }
        } while (c0_ == '/');
        return hasLetters ? TokenType.ATTACHMENT : TokenType.ILLEGAL;
    }

    private TokenType scanHtmlComment() {
        expect('!');
        advance();
        if (c0_ == '-' && advance()) {
            if (c0_ == '-') {
                while (advance()) {
                    if (c0_ == '-' && advance()) {
                        if (c0_ == '-' && advance()) {
                            if (c0_ == '>') {
                                c0_ = ' ';
                                return TokenType.WHITESPACE;
                            }
                        }
                    }
                }
            }
        }

        return TokenType.ILLEGAL;
    }

    private void addLiteralCharAdvance() {
        literal.append(c0_);
        advance();
    }
}