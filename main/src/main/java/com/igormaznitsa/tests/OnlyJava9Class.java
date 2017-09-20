//#excludeif java.version<9
package com.igormaznitsa.tests;

import java.util.List;

public class OnlyJava9Class {
  public List<String> getList() {
    return List.of("one","two","three");
  }
}
