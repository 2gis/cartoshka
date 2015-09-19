package com.github.tartakynov.cartoshka.tokenizers;

import com.github.tartakynov.cartoshka.exceptions.UnexpectedCharException;

import java.util.*;

public abstract class Tokenizer {
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

    private final StringBuilder literal;
    protected char c0_;
    private Token next;
    private Token current;

    protected Tokenizer() {
        literal = new StringBuilder();
        current = null;
    }

    private static boolean isLineTerminator(char c) {
        return c == '\n' || c == '\r';
    }

    protected void initialize() {
        advance();
        skipWhiteSpace();
        scan();
    }

    public Token peek() {
        return next;
    }

    public Token next() {
        current = next;
        scan();

        return current;
    }

    public Token current() {
        return current;
    }

    private void expect(char expected) {
        if (expected != c0_) {
            throw new UnexpectedCharException(c0_, getCurrentPosition());
        }
    }

    protected abstract boolean advance();

    protected abstract int getCurrentPosition();

    protected abstract boolean isEOS();

    protected abstract void push(char c);

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
                case '\'':
                    token = scanString();
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
                    token = select(TokenType.COLON);
                    break;

                case ';':
                    token = select(TokenType.SEMICOLON);
                    break;

                case ',':
                    token = select(TokenType.COMMA);
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
                    token = select(TokenType.HASH);
                    break;

                case '@':
                    token = scanVariable();
                    break;

                default:
                    if (Character.isJavaIdentifierStart(c0_)) {
                        token = scanIdentifierOrKeyword();
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

    private TokenType scanString() {
        char quote = c0_;
        while (advance()) {
            if (isLineTerminator(c0_)) {
                break;
            } else if (c0_ == quote) {
                advance(); // consume quote
                return TokenType.STRING_LITERAL;
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

    private TokenType scanVariable() {
        expect('@');
        advance(); // consume @
        if (Character.isJavaIdentifierStart(c0_)) {
            literal.append(c0_);
            while (advance() && (Character.isJavaIdentifierPart(c0_) || c0_ == '-')) {
                literal.append(c0_);
            }

            return TokenType.VARIABLE;
        }

        return TokenType.ILLEGAL;
    }

    private TokenType scanIdentifierOrKeyword() {
        literal.append(c0_);
        while (advance() && (Character.isJavaIdentifierPart(c0_) || c0_ == '-')) {
            literal.append(c0_);
        }

        TokenType type = KEYWORDS.get(literal.toString());
        if (type != null) {
            return type;
        }

        return TokenType.IDENTIFIER;
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