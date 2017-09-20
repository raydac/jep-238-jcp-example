//#excludeif java.version<9
package com.igormaznitsa.tests;

import java.util.List;

/**
 * The Class will be presented only for Java 9 and great.
 */
public class OnlyJava9Class {
    public List<String> getList() {
        return List.of("one", "two", "three");
    }
}
