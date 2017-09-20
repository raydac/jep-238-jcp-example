package com.igormaznitsa.tests;

/**
 * Just main class which can be called through command line, nothing special. But it contains APi call which provided since JDK 9.
 */
public class Main {

    public static void main(final String... args) {
        // if you want have only one active piece of code, you can use '//$' which tells to preprocessor to uncoment marked string in preprocessing
        // and here I have both variants uncommented

        //#if java.version>8
            // we are in section for JDK 9 and great
            System.out.println("Hello New Java "+Runtime.version().toString()+" !");
        //#else
            // the section for JDK 8 and earlier ones
            System.out.println("Hello Good Old Java!");
        //#endif

        // Just check that class which we want to see only in JDK 9 is provided in scope
        Class<?> java9Class = null;
        try {
            java9Class = Class.forName("com.igormaznitsa.tests.OnlyJava9Class");
        } catch (Exception ex) {
        }
        System.out.println("OnlyJava9Class is " + (java9Class == null ? "not in scope" : "in scope"));
    }
}
