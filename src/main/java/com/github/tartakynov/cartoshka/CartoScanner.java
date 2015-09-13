package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.exceptions.UnexpectedTokenException;
import com.metaweb.lessen.Utilities;
import com.metaweb.lessen.tokenizers.Tokenizer;
import com.metaweb.lessen.tokens.Token;

import java.io.Reader;

abstract class CartoScanner {
    private Token currentToken;

    private Token nextToken;

    private Tokenizer tokenizer;

    protected CartoScanner(Reader input) {
        this.tokenizer = Utilities.open(input);
        this.nextToken = skipWhitespace();
    }

    protected Token peek() {
        return nextToken;
    }

    protected Token accept(Token.Type type) {
        if (nextToken != null && nextToken.type == type) {
            return next();
        }

        return null;
    }

    protected Token accept(Token.Type type, String value) {
        if (nextToken != null && nextToken.type == type && nextToken.text.equalsIgnoreCase(value)) {
            return next();
        }

        return null;
    }

    protected Token expect(Token.Type type) {
        Token token = next();
        if (token != null) {
            if (token.type == type) {
                return token;
            }

            throw new UnexpectedTokenException(token.text, token.start);
        }

        throw new UnexpectedTokenException("EOF");
    }

    protected Token expect(Token.Type type, String value) {
        Token token = next();
        if (token != null) {
            if (token.type == type && token.text.equalsIgnoreCase(value)) {
                return token;
            }

            throw new UnexpectedTokenException(token.text, token.start);
        }

        throw new UnexpectedTokenException("EOF");
    }

    protected Token next() {
        this.currentToken = this.nextToken;
        tokenizer.next();
        this.nextToken = skipWhitespace();
        return this.currentToken;
    }

    private Token skipWhitespace() {
        while (this.tokenizer.getToken() != null && this.tokenizer.getToken().type == Token.Type.Whitespace) {
            this.tokenizer.next();
        }

        return this.tokenizer.getToken();
    }
}
