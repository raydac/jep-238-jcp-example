package com.igormaznitsa.tests;

public class Main {

    public static void main(final String... args) {
        //#if java.version>8
        //$ System.out.println("Hello New Java "+Runtime.version().toString()+" !");
        //#else
        System.out.println("Hello Good Old Java!");
        //#endif
        Class<?> java9Class = null;
        try {
            java9Class = Class.forName("com.igormaznitsa.tests.OnlyJava9Class");
        } catch (Exception ex) {
        }
        System.out.println("OnlyJava9Class is " + (java9Class == null ? "not in scope" : "in scope"));
    }
}
