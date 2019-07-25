package com.hopkins.simpledb.part2;

public class ConstraintViolatedException extends Exception {

  public ConstraintViolatedException(String message) {
    super(message);
  }
}
