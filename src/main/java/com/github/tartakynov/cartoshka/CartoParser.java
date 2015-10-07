package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.exceptions.ArgumentException;
import com.github.tartakynov.cartoshka.exceptions.CartoshkaException;
import com.github.tartakynov.cartoshka.exceptions.UnexpectedTokenException;
import com.github.tartakynov.cartoshka.functions.Functions;
import com.github.tartakynov.cartoshka.scanners.Scanner;
import com.github.tartakynov.cartoshka.scanners.Token;
import com.github.tartakynov.cartoshka.scanners.TokenType;
import com.github.tartakynov.cartoshka.tree.*;
import com.github.tartakynov.cartoshka.tree.entities.*;
import com.github.tartakynov.cartoshka.tree.entities.literals.Boolean;
import com.github.tartakynov.cartoshka.tree.entities.literals.*;

import java.io.Reader;
import java.util.*;

public final class CartoParser extends Scanner {

    private static final int MaxArguments = 32;

    private final Map<String, Function> functions;

    private final Collection<Reader> sources;

    private Context context;

    private boolean includeVariables;

    private boolean foldNodes;

    public CartoParser() {
        this.functions = new HashMap<>();
        this.sources = new LinkedList<>();
        this.context = new Context();
        this.includeVariables = false;
        this.foldNodes = true;
        for (Function function : Functions.BUILTIN_FUNCTIONS) {
            addOrReplaceFunction(function);
        }
    }

    public boolean getIncludeVariables() {
        return includeVariables;
    }

    public void setIncludeVariables(boolean includeVariables) {
        this.includeVariables = includeVariables;
    }

    public boolean getFoldNodes() {
        return foldNodes;
    }

    public void setFoldNodes(boolean foldNodes) {
        this.foldNodes = foldNodes;
    }

    public CartoParser addSource(Reader source) {
        sources.add(source);
        return this;
    }

    public List<Node> parse() {
        List<Node> root = new ArrayList<>();
        for (Reader source : sources) {
            initialize(source);
            root.addAll(parsePrimary());
        }

        if (foldNodes) {
            for (Node node : root) {
                node.fold();
            }
        }

        return root;
    }

    public void addOrReplaceFunction(Function function) {
        functions.put(function.getName(), function);
    }

    // The `primary` rule is the *entry* and *exit* point of the parser.
    // The rules here can appear at any level of the parse tree.
    private List<Node> parsePrimary() {
        List<Node> nodes = new ArrayList<>();
        while (peek().getType() != TokenType.EOS && peek().getType() != TokenType.RBRACE) {
            switch (peek().getType()) {
                case VARIABLE:
                    Rule variable = context.setVariable(parseRule());
                    if (includeVariables) {
                        nodes.add(variable);
                    }

                    break;

                case IDENTIFIER:
                    nodes.add(parseRule());
                    break;

                default:
                    context = context.createNestedBlockContext();
                    nodes.add(parseRuleSet());
                    context = context.getParent();
            }
        }

        return nodes;
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
                return new ExpandableText(context, next().getText());

            case URL:
                return new ExpandableText(context, next().getText(), true);

            case TRUE_LITERAL:
                next();
                return new Boolean(true);

            case FALSE_LITERAL:
                next();
                return new Boolean(false);

            case VARIABLE:
                return new Variable(context, next().getText());

            case DIMENSION_LITERAL:
                return parseDimension();

            case HASH:
                return parseHexColor();

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
                    return parseFunctionCall();
                } else if (Colors.Strings.containsKey(identifier.getText())) {
                    return Colors.Strings.get(identifier.getText());
                }

                return new Keyword(identifier.getText());

            default:
                throw new CartoshkaException(String.format("Unhandled expression %s at %d", peek().getText(), peek().getStart()));
        }
    }

    private Call parseFunctionCall() {
        String functionName = current().getText();
        Function function = functions.get(functionName);
        if (function != null) {
            Collection<Expression> arguments = parseArgumentsExpression();
            if (function.getArgumentCount() != arguments.size()) {
                throw ArgumentException.wrongArgumentsCount(functionName, function.getArgumentCount(), arguments.size());
            }

            return new Call(function, arguments);
        }

        throw new CartoshkaException(String.format("Function [%s] not found", functionName));
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
        if (left.isLiteral()) {
            Literal literal = (Literal) left;
            if (literal.isKeyword()) {
                if (literal.toString().equals(TokenType.ZOOM_KEYWORD.getStr())) {
                    return new Zoom(op.getType(), left, right);
                }
            }
        }

        return new Filter(op.getType(), left, right);
    }

    private Token expect(TokenType... types) {
        Token token = next();
        for (TokenType type : types) {
            if (token.getType() == type) {
                return token;
            }
        }

        throw new UnexpectedTokenException(token.getType().name(), token.getStart());
    }
}