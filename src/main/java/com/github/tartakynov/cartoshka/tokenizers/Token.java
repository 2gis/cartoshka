package com.github.tartakynov.cartoshka.tokenizers;

public class Token {
    private final TokenType type;
    private final int start;
    private final int end;
    private final String text;

    public Token(TokenType type, int start, int end, String text) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public TokenType getType() {
        return type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getText() {
        return text;
    }
}