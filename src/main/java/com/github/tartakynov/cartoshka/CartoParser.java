package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.exceptions.UnexpectedTokenException;
import com.github.tartakynov.cartoshka.tree.Comment;
import com.github.tartakynov.cartoshka.tree.Node;
import com.github.tartakynov.cartoshka.tree.Quoted;
import com.metaweb.lessen.tokens.Token;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CartoParser extends CartoScanner {
    private CartoParser(Reader input) {
        super(input);
    }

    private Token expect(Token.Type type) {
        Token token = next();
        if (token != null) {
            if (token.type == type) {
                return token;
            }

            throw new UnexpectedTokenException(token.text, token.start);
        }

        throw new UnexpectedTokenException("EOF");
    }

    private Token expect(Token.Type type, String value) {
        Token token = next();
        if (token != null) {
            if (token.type == type && token.text.equalsIgnoreCase(value)) {
                return token;
            }

            throw new UnexpectedTokenException(token.text, token.start);
        }

        throw new UnexpectedTokenException("EOF");
    }

    // The `primary` rule is the *entry* and *exit* point of the parser.
    // The rules here can appear at any level of the parse tree.
    public Collection<Node> parsePrimary() {
        Node node;
        List<Node> root = new ArrayList<>();

        while (current() != null && (node = Coalesce.of(parseRule(), parseRuleSet(), parseComment())) != null) {
            switch (peek()) {
                case Comment:
                    root.add(parseComment());
            }

            if (node != null) {
                root.add(node);
            }
        }

        return root;
    }

    private Node parseInvalid() {
        throw new NotImplementedException();
    }

    // We create a Comment node for CSS comments `/* */`,
    // but keep the LeSS comments `//` silent, by just skipping
    // over them.
    private Node parseComment() {
        if (current() == Token.Type.Comment) {
            return new Comment(currentToken().text);
        }

        return null;
    }

    // Entities are tokens which can be found inside an Expression
    // -----------------------------------------------------------------

    // A string, which supports escaping " and ' "milky way" 'he\'s the one!'
    private Node parseEntityQuoted() {
        if (current() == Token.Type.String) {
            return new Quoted(currentToken().text);
        }

        return null;
    }

    // A reference to a Mapnik field, like [NAME]
    // Behind the scenes, this has the same representation, but Carto
    // needs to be careful to warn when unsupported operations are used.
    private Node parseEntityField() {
        if (current() == Token.Type.Delimiter) {
            if (!currentToken().text.equals("[")) {
                return null;
            }


            return new Quoted(currentToken().text);
        }

        return null;
    }

    // This is a comparison operator
    private Node parseEntityComparison() {
        throw new NotImplementedException();
    }

    // A catch-all word, such as: hard-light
    // These can start with either a letter or a dash (-),
    // and then contain numbers, underscores, and letters.
    private Node parseEntityKeyword() {
        throw new NotImplementedException();
    }

    // A function call like rgb(255, 0, 255)
    // The arguments are parsed with the `entities.arguments` parser.
    // url() is handled by the url parser instead
    private Node parseEntityCall() {
        throw new NotImplementedException();
    }

    // Arguments are comma-separated expressions
    private Node parseEntityArguments() {
        throw new NotImplementedException();
    }

    private Node parseEntityExpressionList() {
        throw new NotImplementedException();
    }

    private Node parseEntityLiteral() {
        throw new NotImplementedException();
    }

    // Parse url() tokens
    //
    // We use a specific rule for urls, because they don't really behave like
    // standard function calls. The difference is that the argument doesn't have
    // to be enclosed within a string, so it can't be parsed as an Expression.
    private Node parseEntityUrl() {
        throw new NotImplementedException();
    }

    // A Variable entity, such as `@fink`, in
    //
    //     width: @fink + 2px
    //
    // We use a different parser for variable definitions,
    // see `parsers.variable`.
    private Node parseEntityVariable() {
        throw new NotImplementedException();
    }

    private Node parseEntityHexColor() {
        throw new NotImplementedException();
    }

    private Node parseEntityKeywordColor() {
        throw new NotImplementedException();
    }

    // A Dimension, that is, a number and a unit. The only
    // unit that has an effect is %
    private Node parseEntityDimension() {
        throw new NotImplementedException();
    }

    // End of Entities
    // -----------------------------------------------------------------

    // The variable part of a variable definition.
    // Used in the `rule` parser. Like @fink:
    private Node parseVariable() {
        throw new NotImplementedException();
    }

    // Entities are the smallest recognized token,
    // and can be found inside a rule's value.
    private Node parseEntity() {
        throw new NotImplementedException();
    }

    // A Rule terminator. Note that we use `peek()` to check for '}',
    // because the `block` rule will be expecting it, but we still need to make sure
    // it's there, if ';' was ommitted.
    private Node parseEnd() {
        throw new NotImplementedException();
    }

    // Elements are the building blocks for Selectors. They consist of
    // an element name, such as a tag a class, or `*`.
    private Node parseElement() {
        throw new NotImplementedException();
    }

    // Attachments allow adding multiple lines, polygons etc. to an
    // object. There can only be one attachment per selector.
    private Node parseAttachment() {
        throw new NotImplementedException();
    }

    // Selectors are made out of one or more Elements, see above.
    private Node parseSelector() {
        throw new NotImplementedException();
    }

    private Node parseFilter() {
        throw new NotImplementedException();
    }

    private Node parseZoom() {
        throw new NotImplementedException();
    }

    // The `block` rule is used by `ruleset`
    // It's a wrapper around the `primary` rule, with added `{}`.
    private Node parseBlock() {
        throw new NotImplementedException();
    }

    private Node parseRuleSet() {
        throw new NotImplementedException();
    }

    private Node parseRule() {
        throw new NotImplementedException();
    }

    private Node parseFont() {
        throw new NotImplementedException();
    }

    // A Value is a comma-delimited list of Expressions
    // In a Rule, a Value represents everything after the `:`,
    // and before the `;`.
    private Node parseValue() {
        throw new NotImplementedException();
    }

    // A sub-expression, contained by parenthensis
    private Node parseSub() {
        throw new NotImplementedException();
    }

    // This is a misnomer because it actually handles multiplication
    // and division.
    private Node parseMultiplication() {
        throw new NotImplementedException();
    }

    private Node parseAddition() {
        throw new NotImplementedException();
    }

    // An operand is anything that can be part of an operation,
    // such as a Color, or a Variable
    private Node parseOperand() {
        throw new NotImplementedException();
    }

    // Expressions either represent mathematical operations,
    // or white-space delimited Entities.  @var * 2
    private Node parseExpression() {
        throw new NotImplementedException();
    }

    private Node parseProperty() {
        throw new NotImplementedException();
    }
}

