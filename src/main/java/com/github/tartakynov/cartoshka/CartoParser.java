package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.exceptions.UnexpectedTokenException;
import com.github.tartakynov.cartoshka.tree.Comment;
import com.github.tartakynov.cartoshka.tree.Node;
import com.github.tartakynov.cartoshka.tree.Value;
import com.github.tartakynov.cartoshka.tree.VariableDeclaration;
import com.github.tartakynov.cartoshka.tree.entities.*;
import com.github.tartakynov.cartoshka.tree.entities.Boolean;
import com.github.tartakynov.cartoshka.tree.entities.Color;
import com.metaweb.lessen.tokens.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Reader;
import java.util.*;

public final class CartoParser extends CartoScanner {
    private static final Map<String, Integer> precedences = new HashMap<String, Integer>() {{
        put("+", 2);
        put("-", 2);

        put("*", 3);
        put("/", 3);

        put("%", 3);
        put("^", 3);
    }};

    private CartoParser(Reader input) {
        super(input);
    }

    private static int getPrecedence(String token) {
        Integer precedence = precedences.get(token);
        if (precedence != null) {
            return precedence;
        }

        return 0;
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
            switch (token.type) {
                case AtIdentifier:
                    root.add(parseVariable());
                    break;
                case Comment:
                    root.add(parseComment());
                    break;
                default:
                    root.add(parseRuleSet());
            }
        }

        return root;
    }

    // We create a Comment node for CSS comments `/* */`,
    // but keep the LeSS comments `//` silent, by just skipping
    // over them.
    private Comment parseComment() {
        Token token = expect(Token.Type.Comment);
        return new Comment(token.text);
    }

    // The variable part of a variable definition.
    // Used in the `rule` parser. Like @fink:
    private VariableDeclaration parseVariable() {
        Token name = expect(Token.Type.AtIdentifier);
        expect(Token.Type.Delimiter, ":");
        Value value = parseValue();
        expect(Token.Type.Delimiter, ";");

        return new VariableDeclaration(name.text, value);
    }

    // A Value is a comma-delimited list of Expressions
    // In a Rule, a Value represents everything after the `:`,
    // and before the `;`.
    private Value parseValue() {
        Collection<Expression> expressions = new ArrayList<>();
        do {
            expressions.add(parseExpression());
        } while (accept(Token.Type.Delimiter, ",") != null);

        return new Value(expressions);
    }

    // Expressions either represent mathematical operations,
    // or white-space delimited Entities.  @var * 2
    private Expression parseExpression() {
        return parseBinaryExpression(1);
    }

    private Expression parseBinaryExpression(int prec) {
        Expression result = parsePrimaryExpression();
        for (int prec1 = getPrecedence(peek().text); prec1 >= prec; prec1--) {
            while (getPrecedence(peek().text) == prec1) {
                Token operation = expect(Token.Type.Operator);
                Expression right = parseBinaryExpression(prec1 + 1);
                result = new BinaryOperation(operation.text, result, right);
            }
        }

        return result;
    }

    private Expression parsePrimaryExpression() {
        /*
            $(this.entities.call) ||
            $(this.entities.literal) ||
            $(this.entities.field) ||
            $(this.entities.variable) ||
            $(this.entities.url) ||
            $(this.entities.keyword);
         */

        Expression result;
        switch (peek().type) {
            case Function:
                // fname(
                Token f = next();
                String func = f.text;
                result = new Call(func.substring(0, func.length() - 1), parseArgumentsExpression());
                break;

            case Uri:
                result = new Url(((UriToken) next()).unquotedText);
                break;

            case Number:
                result = new Literal(((NumericToken) next()).n);
                break;

            case Identifier:
                Token idToken = next();
                switch (idToken.text) {
                    case "true":
                        result = new Boolean(true);
                        break;
                    case "false":
                        result = new Boolean(false);
                        break;
                    default:
                        result = Colors.Strings.get(idToken.text);
                        if (result == null) {
                            result = new Keyword(idToken.text);
                        }
                }
                break;

            case AtIdentifier:
                result = new Variable(next().text);
                break;

            case HashName:
                Token hexToken = next();
                if (hexToken.text.length() != 7) {
                    throw new UnexpectedTokenException(hexToken.text);
                }

                int r = Integer.parseInt(hexToken.text.substring(1, 3), 16);
                int g = Integer.parseInt(hexToken.text.substring(3, 5), 16);
                int b = Integer.parseInt(hexToken.text.substring(5, 7), 16);
                result = new Color(r, g, b);
                break;

            case Color:
                ComponentColor colorToken = (ComponentColor) next();
                result = new Color(colorToken.getR(), colorToken.getG(), colorToken.getB(), colorToken.getA());
                break;

            case String:
                result = new Quoted(((StringValueToken) next()).unquotedText);
                break;

            case Dimension:
                NumericWithUnitToken dimensionToken = (NumericWithUnitToken) next();
                result = new Dimension(dimensionToken.n, dimensionToken.unit);
                break;

            case Delimiter:
                if (accept(Token.Type.Delimiter, "(") != null) {
                    Expression e = parseExpression();
                    expect(Token.Type.Delimiter, ")");
                    result = e;
                    break;
                }

            default:
                throw new UnexpectedTokenException(peek().text);
        }

        return result;
    }

    private Collection<Expression> parseArgumentsExpression() {
        Collection<Expression> args = new ArrayList<>();
        boolean done = (peek().text.equals(")"));
        while (!done) {
            Expression argument = parseExpression();
            args.add(argument);
            done = (peek().text.equals(")"));
            if (!done) {
                expect(Token.Type.Delimiter, ",");
            }
        }

        expect(Token.Type.Delimiter, ")");
        return args;
    }

//    private Node parseInvalid() {
//        throw new NotImplementedException();
//    }
//
//    // We create a Comment node for CSS comments `/* */`,
//    // but keep the LeSS comments `//` silent, by just skipping
//    // over them.
//    private Node parseComment() {
//        Token token = accept(Token.Type.Comment);
//        if (token != null) {
//            return new Comment(token.text);
//        }
//
//        return null;
//    }

    // Entities are tokens which can be found inside an Expression
    // -----------------------------------------------------------------

    // A string, which supports escaping " and ' "milky way" 'he\'s the one!'
    private Node parseEntityQuoted() {
        throw new NotImplementedException();
    }

    // A reference to a Mapnik field, like [NAME]
    // Behind the scenes, this has the same representation, but Carto
    // needs to be careful to warn when unsupported operations are used.
    private Node parseEntityField() {
        throw new NotImplementedException();
    }

    // This is a comparison operator
    private Node parseEntityComparison() {
        // =~|=|!=|<=|>=|<|>

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

    // A VariableDeclaration entity, such as `@fink`, in
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
    // such as a Color, or a VariableDeclaration
    private Node parseOperand() {
        throw new NotImplementedException();
    }

    private Node parseProperty() {
        throw new NotImplementedException();
    }
}

