package com.hopkins.simpledb.part2.parser;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.hopkins.simpledb.part2.catalog.ColumnValuePair;

import java.util.List;

public class AssignmentClause {
  private final ImmutableList<ColumnValuePair> columnValuePairList;

  public AssignmentClause(List<ColumnValuePair> columnValuePairList) {
    Preconditions.checkArgument(!columnValuePairList.isEmpty());
    this.columnValuePairList = ImmutableList.copyOf(columnValuePairList);
  }

  public ImmutableList<ColumnValuePair> getColumnValuePairList() {
    return columnValuePairList;
  }

  public boolean containsColumn(String columnName) {
    return columnValuePairList.stream().anyMatch(pair -> pair.getColumnName().equalsIgnoreCase(columnName));
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (ColumnValuePair pair : columnValuePairList) {
      builder
          .append(pair.getColumnName()).append(" = '")
          .append(pair.getLiteralValue().replaceAll("'", "\\'")).append("',");
    }
    builder.setLength(builder.length() - 1);
    return builder.toString();
  }
}
