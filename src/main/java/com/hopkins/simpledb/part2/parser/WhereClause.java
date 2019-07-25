package com.hopkins.simpledb.part2.parser;

public class WhereClause {
  private final String columnName;
  private final String literalValue;

  public WhereClause(String columnName, String literalValue) {
    this.columnName = columnName;
    this.literalValue = literalValue;
  }

  public String getColumnName() {
    return columnName;
  }

  public String getLiteralValue() {
    return literalValue;
  }

  @Override
  public String toString() {
    return " WHERE "
        + columnName + " = '" + literalValue.replaceAll("'", "\\'") + "'";
  }
}
