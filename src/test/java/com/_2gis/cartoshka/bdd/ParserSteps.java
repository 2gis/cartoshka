package com._2gis.cartoshka.bdd;

import com._2gis.cartoshka.CartoParser;
import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.tree.*;
import com._2gis.cartoshka.tree.expression.Literal;
import com._2gis.cartoshka.tree.expression.literal.Color;
import com._2gis.cartoshka.visitor.ConstantFoldVisitor;
import com._2gis.cartoshka.visitor.EvaluateVisitor;
import com._2gis.cartoshka.visitor.PrintVisitor;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import java.io.StringReader;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ParserSteps {
    private CartoParser parser = null;
    private List<Node> nodes = null;
    private boolean evaluate = false;
    private PrintVisitor pv = new PrintVisitor(2);

    @Given("a parser")
    public void givenParser() {
        parser = new CartoParser();
        evaluate = false;
    }

    @Given("a parser with constant folding")
    public void givenEvaluatingParser() {
        parser = new CartoParser();
        evaluate = true;
    }

    @When("the following source is parsed:$src")
    public void whenSourceIsParsed(@Named("src") String src) {
        Block style = parser.parse(String.valueOf(src.hashCode()), new StringReader(src.trim()));
        if (evaluate) {
            style.accept(new ConstantFoldVisitor(), null);
        }

        nodes = style.getNodes();
    }

    @Then("rule $rule is:$value")
    public void thenRuleIs(@Named("rule") String rule, @Named("value") String value) {
        Queue<Node> queue = new LinkedBlockingQueue<>(nodes);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node instanceof Rule) {
                Rule r = (Rule) node;
                if (r.getFullName().equals(rule)) {
                    String expected = value.trim();
                    String given = r.getValue().accept(pv, null).trim();
                    Assert.assertEquals(expected, given);
                    return;
                }
            } else if (node instanceof Ruleset) {
                queue.add(((Ruleset) node).getBlock());
            } else if (node instanceof Block) {
                queue.addAll(((Block) node).getNodes());
            }
        }

        Assert.fail(String.format("Rule %s not found", rule));
    }

    @Then("color $rule as hex is:$value")
    public void thenColorAsHex(@Named("rule") String rule, @Named("value") String value) {
        Queue<Node> queue = new LinkedBlockingQueue<>(nodes);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node instanceof Rule) {
                Rule r = (Rule) node;
                if (r.getFullName().equals(rule)) {
                    Literal literal = r.getValue().accept(new EvaluateVisitor(), null);
                    if (literal.type() == NodeType.COLOR) {
                        Color color = (Color) literal;
                        String expected = value.trim();
                        String given = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
                        Assert.assertEquals(expected, given);
                        return;
                    }
                }
            } else if (node instanceof Ruleset) {
                queue.add(((Ruleset) node).getBlock());
            } else if (node instanceof Block) {
                queue.addAll(((Block) node).getNodes());
            }
        }

        Assert.fail(String.format("Color %s not found", rule));
    }

    @Then("variable is undefined")
    public void thenVariableUndefined() {
        try {
            ConstantFoldVisitor ev = new ConstantFoldVisitor();
            for (Node node : nodes) {
                node.accept(ev, null);
            }
        } catch (CartoshkaException ex) {
            if (ex.toString().contains("Undefined variable")) {
                return;
            }
        }

        Assert.fail("No undefined variables");
    }

    private Ruleset getRulesetByNum(int num) {
        Queue<Node> queue = new LinkedBlockingQueue<>(nodes);
        int index = 0;
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node instanceof Ruleset) {
                index++;
                Ruleset ruleset = (Ruleset) node;
                if (index == num) {
                    return ruleset;
                } else {
                    queue.add(ruleset.getBlock());
                }
            } else if (node instanceof Block) {
                queue.addAll(((Block) node).getNodes());
            }
        }

        return null;
    }

    @Then("ruleset $num contains classes:$classes")
    public void thenRulesetContainsClasses(@Named("num") int num, @Named("classes") String classes) {
        Ruleset ruleset = getRulesetByNum(num);
        Assert.assertNotNull(String.format("Ruleset %d not found", num), ruleset);
        StringBuilder sb = new StringBuilder();
        for (Selector selector : ruleset.getSelectors()) {
            for (Element element : selector.getElements()) {
                if (element.getType() == Element.ElementType.CLASS) {
                    sb.append(element.accept(pv, null));
                    sb.append(", ");
                }
            }
        }

        sb.setLength(sb.length() - 2);
        Assert.assertEquals(classes.trim(), sb.toString());
    }

    @Then("ruleset $num contains ids:$ids")
    public void thenRulesetContainsIds(@Named("num") int num, @Named("ids") String ids) {
        Ruleset ruleset = getRulesetByNum(num);
        Assert.assertNotNull(String.format("Ruleset %d not found", num), ruleset);
        StringBuilder sb = new StringBuilder();
        for (Selector selector : ruleset.getSelectors()) {
            for (Element element : selector.getElements()) {
                if (element.getType() == Element.ElementType.ID) {
                    sb.append(element.accept(pv, null));
                    sb.append(", ");
                }
            }
        }

        sb.setLength(sb.length() - 2);
        Assert.assertEquals(ids.trim(), sb.toString());
    }

    @Then("ruleset $num contains attachments:$attachments")
    public void thenRulesetContainsAttachment(@Named("num") int num, @Named("$attachments") String attachments) {
        Ruleset ruleset = getRulesetByNum(num);
        Assert.assertNotNull(String.format("Ruleset %d not found", num), ruleset);
        StringBuilder sb = new StringBuilder();
        for (Selector selector : ruleset.getSelectors()) {
            sb.append(selector.getAttachment());
            sb.append(", ");
        }

        sb.setLength(sb.length() - 2);
        Assert.assertEquals(attachments.trim(), sb.toString());
    }

    @Then("ruleset $num contains filters:$filters")
    public void thenRulesetContainsFilters(@Named("num") int num, @Named("filters") String filters) {
        Ruleset ruleset = getRulesetByNum(num);
        Assert.assertNotNull(String.format("Ruleset %d not found", num), ruleset);
        StringBuilder sb = new StringBuilder();
        for (Selector selector : ruleset.getSelectors()) {
            for (Filter filter : selector.getFilters()) {
                sb.append(filter.accept(pv, null));
                sb.append(", ");
            }
        }

        sb.setLength(sb.length() - 2);
        Assert.assertEquals(filters.trim(), sb.toString());
    }
}