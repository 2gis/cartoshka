package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.exceptions.UnexpectedTokenException;
import com.github.tartakynov.cartoshka.tokenizers.Token;
import com.github.tartakynov.cartoshka.tokenizers.TokenType;
import com.github.tartakynov.cartoshka.tree.Node;
import com.github.tartakynov.cartoshka.tree.Value;
import com.github.tartakynov.cartoshka.tree.VariableDeclaration;
import com.github.tartakynov.cartoshka.tree.entities.*;
import com.github.tartakynov.cartoshka.tree.entities.Boolean;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CartoParser extends CartoScanner {
    private static final int MaxArguments = 32;

    private CartoParser(Reader input) {
        super(input);
    }

    public static Collection<Node> parse(Reader input) {
        CartoParser parser = new CartoParser(input);
        return parser.parsePrimary();
    }

    // The `primary` rule is the *entry* and *exit* point of the parser.
    // The rules here can appear at any level of the parse tree.
    private Collection<Node> parsePrimary() {
        List<Node> root = new ArrayList<>();
        while (peek() != null) {
            Token token = peek();
            switch (token.getType()) {
                case VARIABLE:
                    root.add(parseVariable());
                    break;
                default:
                    root.add(parseRuleSet());
            }
        }

        return root;
    }

    // The variable part of a variable definition.
    // Used in the `rule` parser. Like @fink:
    private VariableDeclaration parseVariable() {
        Token name = expect(TokenType.VARIABLE);
        expect(TokenType.COLON);
        Value value = parseValue();
        expect(TokenType.SEMICOLON);

        return new VariableDeclaration(name.getText(), value);
    }

    // A Value is a comma-delimited list of Expressions
    // In a Rule, a Value represents everything after the `:`,
    // and before the `;`.
    private Value parseValue() {
        Collection<Expression> expressions = new ArrayList<>();
        while (true) {
            expressions.add(parseExpression());
            if (peek().getType() != TokenType.COMMA) {
                break;
            }

            next(); // consume comma
        }

        return new Value(expressions);
    }

    // Expressions either represent mathematical operations,
    // or white-space delimited Entities.  @var * 2
    private Expression parseExpression() {
        return parseBinaryExpression(1);
    }

    private Expression parseBinaryExpression(int prec) {
        Expression result = parseUnaryExpression();
        for (int prec1 = peek().getType().getPrecedence(); prec1 >= prec; prec1--) {
            while (peek().getType().getPrecedence() == prec1) {
                Token operation = next();
                Expression right = parseBinaryExpression(prec1 + 1);
                result = new BinaryOperation(operation.getText(), result, right);
            }
        }

        return result;
    }

    private Expression parseUnaryExpression() {
        switch (peek().getType()) {
            case ADD:
            case SUB:
                Token op = next();
                Expression expression = parseUnaryExpression();
                return new UnaryOperation(op.getText(), expression);
            default:
                return parsePrimaryExpression();
        }
    }

    private Expression parsePrimaryExpression() {
        switch (peek().getType()) {
            case NUMBER_LITERAL:
                return new Literal(Double.valueOf(next().getText()));
            case STRING_LITERAL:
                return new Quoted(next().getText());
            case TRUE_LITERAL:
                next();
                return new Boolean(true);
            case FALSE_LITERAL:
                next();
                return new Boolean(false);
            case VARIABLE:
                return new Variable(next().getText());
            case DIMENSION_LITERAL:
                return parseDimension();
            case HASH:
                return parseHexColor();
            case IDENTIFIER:
                Token identifier = next();
                if (peek().getType() == TokenType.LPAREN) {
                    if (identifier.getText().equals("url")) {

                    }

                    return new Call(identifier.getText(), parseArgumentsExpression());
                } else if (Colors.Strings.containsKey(identifier.getText())) {
                    return Colors.Strings.get(identifier.getText());
                }

                return new Keyword(identifier.getText());
//            case Function:
//                // fname(
//                Token f = next();
//                String func = f.text;
//                result = new Call(func.substring(0, func.length() - 1), parseArgumentsExpression());
//                break;
//
//            case Uri:
//                result = new Url(((UriToken) next()).unquotedText);
//                break;

        }

        throw new NotImplementedException();
    }

    private Dimension parseDimension() {
        Token token = expect(TokenType.DIMENSION_LITERAL);
        String value = token.getText();
        for (int i = 1; i <= 2; i++) {
            if (DIMENSION_UNITS.contains(value.substring(value.length() - i))) {
                String num = value.substring(0, value.length() - i);
                String unit = value.substring(value.length() - i);
                return new Dimension(Double.valueOf(num), unit);
            }
        }

        throw new CartoshkaException(String.format("Wrong unit for dimension at pos: %d", token.getStart()));
    }

    private Color parseHexColor() {
        expect(TokenType.HASH);
        Token token = expect(TokenType.NUMBER_LITERAL);
        String text = token.getText();
        try {
            if (text.length() == 3) {
                int r = Integer.parseInt(text.substring(0, 1) + text.substring(0, 1), 16);
                int g = Integer.parseInt(text.substring(1, 2) + text.substring(1, 2), 16);
                int b = Integer.parseInt(text.substring(2, 3) + text.substring(2, 3), 16);
                return new Color(r, g, b);
            } else if (text.length() == 7) {
                int r = Integer.parseInt(text.substring(0, 2), 16);
                int g = Integer.parseInt(text.substring(2, 4), 16);
                int b = Integer.parseInt(text.substring(4, 6), 16);
                return new Color(r, g, b);
            }
        } catch (NumberFormatException ex) {
            // do nothing
        }

        throw new CartoshkaException(String.format("Wrong hex color %s at pos: %d", token.getText(), token.getStart()));
    }

    private Collection<Expression> parseArgumentsExpression() {
        Collection<Expression> args = new ArrayList<>();
        expect(TokenType.LPAREN);
        boolean done = peek().getType() == TokenType.RPAREN;
        while (!done) {
            Expression argument = parseExpression();
            args.add(argument);
            if (args.size() > MaxArguments) {
                throw new CartoshkaException("too_many_arguments");
            }

            done = peek().getType() == TokenType.RPAREN;
            if (!done) {
                expect(TokenType.COMMA);
            }
        }

        expect(TokenType.RPAREN);
        return args;
    }

    private Node parseRuleSet() {
        throw new NotImplementedException();
    }

    // Selectors are made out of one or more Elements, see above.
    private Collection<Node> parseSelectors() {
        throw new NotImplementedException();
    }

    private Node parseSelector() {
        // element
        // zoom
        // filter
        // attachment
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

    private Node parseRule() {
        throw new NotImplementedException();
    }

    private Node parseFont() {
        throw new NotImplementedException();
    }

    private Node parseProperty() {
        throw new NotImplementedException();
    }

    protected Token expect(TokenType type) {
        Token token = next();
        if (token.getType() != type) {
            throw new UnexpectedTokenException(token.getText(), token.getStart());
        }

        return token;
    }
}

