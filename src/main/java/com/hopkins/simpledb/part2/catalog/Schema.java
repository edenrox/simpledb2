package com.hopkins.simpledb.part2.catalog;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Schema {
  public static Schema EMPTY_SCHEMA = new Schema(ImmutableList.of());

  private ImmutableList<Column> columns;

  public Schema(List<Column> columns) {
    this.columns = ImmutableList.copyOf(columns);
  }

  public ImmutableList<Column> getColumns() {
    return columns;
  }

  public boolean hasColumn(String columnName) {
    return indexOf(columnName) >= 0;
  }

  public int indexOf(String columnName) {
    for (int i = 0; i < columns.size(); i++) {
      if (columns.get(i).getName().equalsIgnoreCase(columnName)) {
        return i;
      }
    }
    return -1;
  }
}
