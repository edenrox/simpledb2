package com.hopkins.simpledb.part2.catalog;

import com.google.common.base.Preconditions;

public class ColumnValuePair {
  private final String columnName;
  private final String literalValue;

  public ColumnValuePair(String columnName, String literalValue) {
    this.columnName = Preconditions.checkNotNull(columnName);
    this.literalValue = literalValue;
  }

  public String getColumnName() {
    return columnName;
  }

  public String getLiteralValue() {
    return literalValue;
  }
}
