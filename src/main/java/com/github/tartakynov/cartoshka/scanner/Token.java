package com.github.tartakynov.cartoshka.scanner;

public class Token {
    private final TokenType type;
    private final String text;
    private final Location location;

    public Token(TokenType type, String text, Location location) {
        this.type = type;
        this.text = text;
        this.location = location;
    }

    public TokenType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public String getText() {
        return text;
    }
}