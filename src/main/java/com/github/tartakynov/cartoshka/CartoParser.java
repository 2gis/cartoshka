package com.github.tartakynov.cartoshka;

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

        }

        throw new NotImplementedException();
    }

//
//    private Expression parsePrimaryExpression() {
//        /*
//            $(this.entities.call) ||
//            $(this.entities.literal) ||
//            $(this.entities.field) ||
//            $(this.entities.variable) ||
//            $(this.entities.url) ||
//            $(this.entities.keyword);
//         */
//
//        Expression result;
//        switch (peek().getType()) {
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
//
//            case Number:
//                result = new Literal(((NumericToken) next()).n);
//                break;
//
//            case Identifier:
//                Token idToken = next();
//                switch (idToken.text) {
//                    case "true":
//                        result = new Boolean(true);
//                        break;
//                    case "false":
//                        result = new Boolean(false);
//                        break;
//                    default:
//                        result = Colors.Strings.get(idToken.text);
//                        if (result == null) {
//                            result = new Keyword(idToken.text);
//                        }
//                }
//                break;
//
//            case VARIABLE:
//                result = new Variable(next().text);
//                break;
//
//            case HASHNAME:
//                Token hexToken = next();
//                if (hexToken.text.length() != 7) {
//                    throw new UnexpectedTokenException(hexToken.text);
//                }
//
//                int r = Integer.parseInt(hexToken.text.substring(1, 3), 16);
//                int g = Integer.parseInt(hexToken.text.substring(3, 5), 16);
//                int b = Integer.parseInt(hexToken.text.substring(5, 7), 16);
//                result = new Color(r, g, b);
//                break;
//
//            case Color:
//                ComponentColor colorToken = (ComponentColor) next();
//                result = new Color(colorToken.getR(), colorToken.getG(), colorToken.getB(), colorToken.getA());
//                break;
//
//            case String:
//                result = new Quoted(((StringValueToken) next()).unquotedText);
//                break;
//
//            case Dimension:
//                NumericWithUnitToken dimensionToken = (NumericWithUnitToken) next();
//                result = new Dimension(dimensionToken.n, dimensionToken.unit);
//                break;
//
//            case Delimiter:
//                if (accept(Token.Type.Delimiter, "(") != null) {
//                    Expression e = parseExpression();
//                    expect(Token.Type.Delimiter, ")");
//                    result = e;
//                    break;
//                }
//
//            default:
//                throw new UnexpectedTokenException(peek().text);
//        }
//
//        return result;
//    }
//
//    private Collection<Expression> parseArgumentsExpression() {
//        Collection<Expression> args = new ArrayList<>();
//        expect(TokenType.LPAREN);
//        boolean done = peek().getType() == TokenType.RPAREN;
//        while (!done) {
//            Expression argument = parseExpression();
//            args.add(argument);
//            if (args.size() > MaxArguments) {
//                throw new CartoshkaException("too_many_arguments");
//            }
//
//            done = peek().getType() == TokenType.RPAREN;
//            if (!done) {
//                expect(TokenType.COMMA);
//            }
//        }
//
//        expect(TokenType.RPAREN);
//        return args;
//    }

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
        Token next = next();
        if (next.getType() != type) {
            throw new UnexpectedTokenException(next.getText(), next.getStart());
        }

        return next;
    }
}

