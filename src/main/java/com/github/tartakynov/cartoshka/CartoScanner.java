package com.github.tartakynov.cartoshka;

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
        this.nextToken = tokenizer.getToken();
    }

    protected Token next() {
        this.currentToken = this.nextToken;
        tokenizer.next();
        this.nextToken = this.tokenizer.getToken();
        return this.currentToken;
    }

    protected Token.Type current() {
        if (this.currentToken != null) {
            return this.currentToken.type;
        }

        return null;
    }

    protected Token.Type peek() {
        if (this.nextToken != null) {
            return this.nextToken.type;
        }

        return null;
    }

    protected Token currentToken() {
        return this.currentToken;
    }

    protected Token peekToken() {
        return this.nextToken;
    }
}
