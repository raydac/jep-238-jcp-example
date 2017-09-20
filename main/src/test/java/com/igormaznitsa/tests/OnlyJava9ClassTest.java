//#excludeif java.version<9
package com.igormaznitsa.tests;

import static org.junit.Assert.*;
import  org.junit.*;

public class OnlyJava9ClassTest {
    @Test
    public void testGetList() {
        Assert.assertArrayEquals(new String[]{"one","two","three"},new OnlyJava9Class().getList().toArray());
    }
}