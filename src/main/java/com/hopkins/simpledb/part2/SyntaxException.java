package com.hopkins.simpledb.part2;

public class SyntaxException extends Exception {
  public static SyntaxException newInstance(String message, String input, int position) {
    return new SyntaxException(message + "\nat: " + input.substring(position));
  }

  public SyntaxException(String message) {
    super(message);
  }
}
