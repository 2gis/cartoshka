package com._2gis.cartoshka.bdd;

import com._2gis.cartoshka.CartoParser;
import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.tree.Node;
import com._2gis.cartoshka.tree.Rule;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import java.io.StringReader;
import java.util.List;

public class ParserSteps {
    private CartoParser parser = null;
    private List<Node> nodes = null;

    @Given("a parser without folding")
    public void givenParserWithoutFolding() {
        parser = new CartoParser();
        parser.setFoldNodes(false);
        parser.setIncludeVariables(true);
    }

    @Given("a parser with folding")
    public void givenParserWithFolding() {
        parser = new CartoParser();
        parser.setFoldNodes(true);
        parser.setIncludeVariables(true);
    }

    @When("the following source is parsed:$src")
    public void whenSourceIsParsed(@Named("src") String src) {
        parser.addSource(String.valueOf(src.hashCode()), new StringReader(src.trim()));
        nodes = parser.parse();
    }

    @Then("rule $rule is:$value")
    public void thenRuleIs(@Named("rule") String rule, @Named("value") String value) {
        for (Node node : nodes) {
            if (node instanceof Rule) {
                Rule r = (Rule) node;
                if (r.getName().equals(rule)) {
                    String expected = value.trim();
                    String given = r.getValue().toString().trim();
                    Assert.assertEquals(expected, given);
                    return;
                }
            }
        }

        Assert.fail(String.format("Rule %s not found", rule));
    }

    @Then("rule $rule evaluates into:$value")
    public void thenEvaluatesInto(@Named("rule") String rule, @Named("value") String value) {
        for (Node node : nodes) {
            if (node instanceof Rule) {
                Rule r = (Rule) node;
                if (r.getName().equals(rule)) {
                    String expected = value.trim();
                    String given = r.getValue().ev(null).toString().trim();
                    Assert.assertEquals(expected, given);
                    return;
                }
            }
        }

        Assert.fail(String.format("Rule %s not found", rule));
    }

    @Then("variable is undefined")
    public void thenVariableUndefined() {
        try {
            for (Node node : nodes) {
                node.fold();
            }
        } catch (CartoshkaException ex) {
            if (ex.toString().contains("Undefined variable")) {
                return;
            }
        }

        Assert.fail("No undefined variables");
    }
}