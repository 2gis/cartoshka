package com.github.tartakynov.cartoshka;

public final class Coalesce {
    public static <T> T of(T one, T two) {
        return one != null ? one : two;
    }

    public static <T> T of(T one, T two, T three) {
        return of(of(one, two), three);
    }

    public static <T> T of(T one, T two, T three, T four) {
        return of(of(one, two, three), four);
    }

    public static <T> T of(T one, T two, T three, T four, T five) {
        return of(of(one, two, three, four), five);
    }
}
