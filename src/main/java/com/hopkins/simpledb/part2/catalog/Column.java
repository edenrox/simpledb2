package com.hopkins.simpledb.part2.catalog;

public class Column {
  private final String name;

  public Column(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public ColumnType getType() {
    return ColumnType.TEXT;
  }
}
