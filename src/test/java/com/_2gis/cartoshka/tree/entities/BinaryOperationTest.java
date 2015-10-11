package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.entities.literals.Color;
import com._2gis.cartoshka.tree.entities.literals.Dimension;
import com._2gis.cartoshka.tree.entities.literals.Numeric;
import org.junit.Assert;
import org.junit.Test;

public class BinaryOperationTest {
    @Test
    public void testColorDimensionCommutative() {
        Literal result;
        Color colorResult;
        Color color = Color.fromRGBA(null, 1, 2, 3, 0.5);
        Dimension dimension = new Dimension(null, 50, "%");

        result = new BinaryOperation(null, TokenType.ADD, color, dimension).ev(null);
        Assert.assertTrue(result.isColor());
        colorResult = (Color) result;
        Assert.assertEquals(1 + 127, colorResult.getRed());
        Assert.assertEquals(2 + 127, colorResult.getGreen());
        Assert.assertEquals(3 + 127, colorResult.getBlue());
        Assert.assertEquals(0.5, colorResult.getAlpha(), 1e-8);

        result = new BinaryOperation(null, TokenType.ADD, dimension, color).ev(null);
        Assert.assertTrue(result.isColor());
        colorResult = (Color) result;
        Assert.assertEquals(1 + 127, colorResult.getRed());
        Assert.assertEquals(2 + 127, colorResult.getGreen());
        Assert.assertEquals(3 + 127, colorResult.getBlue());
        Assert.assertEquals(0.5, colorResult.getAlpha(), 1e-8);
    }

    @Test
    public void testNumberDimensionCommutative() {
        Literal result;
        Dimension dimensionResult;
        Numeric numeric = new Numeric(null, 1, false);
        Dimension dimension = new Dimension(null, 50, "%");

        result = new BinaryOperation(null, TokenType.ADD, numeric, dimension).ev(null);
        Assert.assertTrue(result.isDimension());
        dimensionResult = (Dimension) result;
        Assert.assertEquals(51, dimensionResult.getValue(), 1e-8);
        Assert.assertEquals("%", dimensionResult.getUnit());

        result = new BinaryOperation(null, TokenType.ADD, dimension, numeric).ev(null);
        Assert.assertTrue(result.isDimension());
        dimensionResult = (Dimension) result;
        Assert.assertEquals(51, dimensionResult.getValue(), 1e-8);
        Assert.assertEquals("%", dimensionResult.getUnit());
    }
}
