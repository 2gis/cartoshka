package com.github.tartakynov.cartoshka.tokenizers;

public enum TokenType {
    /* End of source indicator. */
    EOS("EOS", 0),

    /* Punctuators. */
    LPAREN("(", 0),
    RPAREN(")", 0),
    SEMICOLON(";", 0),
    COMMA(",", 1),

    /* Binary operators sorted by precedence. */
    /* IsBinaryOp() relies on this block of enum values */
    /* being contiguous and sorted in the same order! */
    ADD("+", 12),
    SUB("-", 12),
    MUL("*", 13),
    DIV("/", 13),
    MOD("%", 13),
    POW("^", 13),

    /* Compare operators sorted by precedence. */
    /* IsCompareOp() relies on this block of enum values */
    /* being contiguous and sorted in the same order! */
    EQ("=", 9),
    NE("!=", 9),
    LT("<", 10),
    GT(">", 10),
    LTE("<=", 10),
    GTE(">=", 10),

    /* Unary operators. */
    /* IsUnaryOp() relies on this block of enum values */
    /* being contiguous and sorted in the same order! */
    FACTORIAL("!", 0),

    /* Literals. */
    NUMBER_LITERAL(null, 0),
    NULL_LITERAL("null", 0),
    TRUE_LITERAL("true", 0),
    FALSE_LITERAL("false", 0),

    /* Keywords. */
    MAP("Map", 0),
    ZOOM("zoom", 0),

    /* Identifiers (not keywords or future reserved words). */
    IDENTIFIER(null, 0),

    /* Illegal token - not able to scan. */

    ILLEGAL("ILLEGAL", 0),

    /* Scanner-internal use only. */
    WHITESPACE(null, 0);

    private final String str;
    private final int precedence;

    TokenType(String value, int precedence) {
        this.str = value;
        this.precedence = precedence;
    }

    public String getStr() {
        return str;
    }

    public int getPrecedence() {
        return precedence;
    }
}
