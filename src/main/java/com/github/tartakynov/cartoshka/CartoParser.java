package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.exceptions.UnexpectedTokenException;
import com.github.tartakynov.cartoshka.scanners.Token;
import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.*;
import com.github.tartakynov.cartoshka.tree.entities.*;
import com.github.tartakynov.cartoshka.tree.entities.literals.Boolean;
import com.github.tartakynov.cartoshka.tree.entities.literals.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Reader;
import java.util.*;

public final class CartoParser extends CartoScanner {
    private static final int MaxArguments = 32;
    private final Map<String, Function> functions = new HashMap<>();

    private CartoParser(Reader input) {
        super(input);
    }

    public static Collection<Node> parse(Reader input) {
        CartoParser parser = new CartoParser(input);
        parser.initialize();

        return parser.parsePrimary();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            for (java.lang.reflect.Field field : Functions.class.getFields()) {
                Object value = field.get(null);
                if (value instanceof Function) {
                    Function f = (Function) value;
                    functions.put(f.getName(), f);
                }
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addOrReplaceFunction(Function function) {
        functions.put(function.getName(), function);
    }

    // The `primary` rule is the *entry* and *exit* point of the parser.
    // The rules here can appear at any level of the parse tree.
    private Collection<Node> parsePrimary() {
        List<Node> root = new ArrayList<>();
        while (peek().getType() != TokenType.EOS && peek().getType() != TokenType.RBRACE) {
            switch (peek().getType()) {
                case VARIABLE:
                case IDENTIFIER:
                    root.add(parseRule());
                    break;
                default:
                    root.add(parseRuleSet());
            }
        }

        return root;
    }

    private Rule parseRule() {
        Token token = expect(TokenType.VARIABLE, TokenType.IDENTIFIER);
        expect(TokenType.COLON);
        Value value = parseValue();
        expect(TokenType.SEMICOLON);
        return new Rule(token.getText(), value, token.getType() == TokenType.VARIABLE);
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

            expect(TokenType.COMMA); // consume comma
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
                Token op = expect(TokenType.ADD, TokenType.SUB, TokenType.MUL, TokenType.DIV, TokenType.MOD);
                Expression right = parseBinaryExpression(prec1 + 1);
                result = new BinaryOperation(op.getType(), result, right);
            }
        }

        return result;
    }

    private Expression parseUnaryExpression() {
        switch (peek().getType()) {
            case ADD:
                next();
                return parseUnaryExpression();

            case SUB:
                Token op = next();
                Expression expression = parseUnaryExpression();
                return new UnaryOperation(op.getType(), expression);

            default:
                return parsePrimaryExpression();
        }
    }

    private Expression parsePrimaryExpression() {
        switch (peek().getType()) {
            case NUMBER_LITERAL:
                String number = next().getText();
                return new Numeric(Double.valueOf(number), number.indexOf('.') >= 0);

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

            case URL:
                return new Url(next().getText());

            case LBRACK:
                expect(TokenType.LBRACK);
                Token field = next();
                expect(TokenType.RBRACK);
                return new Field(field.getText());

            case LPAREN:
                expect(TokenType.LPAREN);
                Expression expression = parseExpression();
                expect(TokenType.RPAREN);
                return expression;

            case MAP_KEYWORD:
            case ZOOM_KEYWORD:
            case IDENTIFIER:
                Token identifier = next();
                if (peek().getType() == TokenType.LPAREN) {
                    String functionName = identifier.getText();
                    Function function = functions.get(functionName);
                    if (function != null) {
                        return new Call(function, parseArgumentsExpression());
                    }

                    throw new CartoshkaException(String.format("Function [%s] not found", functionName));
                } else if (Colors.Strings.containsKey(identifier.getText())) {
                    return Colors.Strings.get(identifier.getText());
                }

                return new Keyword(identifier.getText());

            default:
                throw new CartoshkaException(String.format("Unhandled expression %s at %d", peek().getText(), peek().getStart()));
        }
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
        Token token = expect(TokenType.HASH);
        String text = token.getText();
        try {
            if (text.length() == 3) {
                int r = Integer.parseInt(text.substring(0, 1) + text.substring(0, 1), 16);
                int g = Integer.parseInt(text.substring(1, 2) + text.substring(1, 2), 16);
                int b = Integer.parseInt(text.substring(2, 3) + text.substring(2, 3), 16);
                return Color.fromRGBA(r, g, b, 1.0);
            } else if (text.length() == 6) {
                int r = Integer.parseInt(text.substring(0, 2), 16);
                int g = Integer.parseInt(text.substring(2, 4), 16);
                int b = Integer.parseInt(text.substring(4, 6), 16);
                return Color.fromRGBA(r, g, b, 1.0);
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            // do nothing
        }

        throw new CartoshkaException(String.format("Wrong hex color #%s at pos: %d", token.getText(), token.getStart()));
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
        // selectors block
        Collection<Selector> selectors = parseSelectors();
        Collection<Node> rules = parseBlock();
        return new Ruleset(selectors, rules);
    }

    private Collection<Node> parseBlock() {
        expect(TokenType.LBRACE);
        Collection<Node> rules = parsePrimary();
        expect(TokenType.RBRACE);
        return rules;
    }

    // Selectors are made out of one or more Elements, see above.
    private Collection<Selector> parseSelectors() {
        Collection<Selector> selectors = new ArrayList<>();
        while (true) {
            selectors.add(parseSelector());
            if (peek().getType() != TokenType.COMMA) {
                break;
            }

            expect(TokenType.COMMA); // consume comma
        }

        return selectors;
    }

    private Selector parseSelector() {
        // (element | zoom | filter)+ attachment?
        boolean done = false;
        Collection<Element> elements = new ArrayList<>();
        Collection<Zoom> zooms = new ArrayList<>();
        Collection<Filter> filters = new ArrayList<>();
        String attachment = null;
        int segments = 0;
        while (!done) {
            switch (peek().getType()) {
                case HASH:
                case PERIOD:
                case MUL:
                case MAP_KEYWORD:
                    elements.add(parseElement());
                    break;

                case LBRACK:
                    Filter filter = parseZoomOrFilter();
                    if (filter instanceof Zoom) {
                        zooms.add((Zoom) filter);
                    } else {
                        filters.add(filter);
                    }

                    break;

                case ATTACHMENT:
                    if (attachment != null) {
                        throw new UnexpectedTokenException(peek().getText(), peek().getStart());
                    }

                    attachment = next().getText();
                    break;

                default:
                    if (segments == 0) {
                        throw new CartoshkaException(String.format("Selector without segments at %d", peek().getEnd()));
                    }

                    done = true;
                    break;
            }

            segments++;
        }

        return new Selector(elements, filters, zooms, attachment);
    }

    // Elements are the building blocks for Selectors. They consist of
    // an element name, such as a tag a class, or `*`.
    private Element parseElement() {
        Token token = expect(TokenType.HASH, TokenType.PERIOD, TokenType.MUL, TokenType.MAP_KEYWORD);
        switch (token.getType()) {
            case HASH:
                return new Element(token.getText(), Element.ElementType.ID);

            case PERIOD:
                return new Element(next().getText(), Element.ElementType.CLASS);

            case MUL:
                return new Element("*", Element.ElementType.ID);
        }

        return new Element(token.getText(), Element.ElementType.MAP);
    }

    private Filter parseZoomOrFilter() {
        expect(TokenType.LBRACK);
        Expression left = parsePrimaryExpression();
        Token op = expect(TokenType.EQ, TokenType.NE, TokenType.LT, TokenType.GT, TokenType.LTE, TokenType.GTE);
        Expression right = parseExpression();
        expect(TokenType.RBRACK);
        if (left instanceof Literal) {
            Literal literal = (Literal) left;
            if (literal.isKeyword()) {
                if (literal.toString().equals(TokenType.ZOOM_KEYWORD.getStr())) {
                    return new Zoom(op.getType(), left, right);
                }
            } else if (literal.isQuoted()) {
                left = new Field(literal.toString());
            }
        }

        return new Filter(op.getType(), left, right);
    }

    private Node parseFont() {
        throw new NotImplementedException();
    }

    protected Token expect(TokenType... types) {
        Token token = next();
        for (TokenType type : types) {
            if (token.getType() == type) {
                return token;
            }
        }

        throw new UnexpectedTokenException(token.getType().name(), token.getStart());
    }
}

