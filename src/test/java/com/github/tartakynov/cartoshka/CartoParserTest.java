package com.github.tartakynov.cartoshka;

import org.junit.Test;

public class CartoParserTest {
    @Test
    public void test() {
        String text = "faf";
        if (text.length() == 3) {
            int r = Integer.parseInt(text.substring(0, 1) + text.substring(0, 1), 16);
            int g = Integer.parseInt(text.substring(1, 2) + text.substring(1, 2), 16);
            int b = Integer.parseInt(text.substring(2, 3) + text.substring(2, 3), 16);
            System.out.println(String.format("%d %d %d", r, g, b));
        } else if (text.length() == 6) {
            int r = Integer.parseInt(text.substring(0, 2), 16);
            int g = Integer.parseInt(text.substring(2, 4), 16);
            int b = Integer.parseInt(text.substring(4, 6), 16);
            System.out.println(String.format("%d %d %d", r, g, b));
        }
    }
}