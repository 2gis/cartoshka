package com._2gis.cartoshka.bdd;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

public class Steps {
    int x;

    @Given("a variable x with value $value")
    public void givenVariable(@Named("value") int value) {
        x = value;
    }

    @When("I multiply x by $value")
    public void whenMultiply(@Named("value") int value) {
        x = x * value;
    }

    @Then("x should equal $value")
    public void thenShouldBe(@Named("value") int value) {
        Assert.assertEquals(value, x);
    }
}
