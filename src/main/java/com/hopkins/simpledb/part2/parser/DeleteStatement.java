package com.hopkins.simpledb.part2.parser;

import com.google.common.base.Preconditions;
import com.hopkins.simpledb.part2.SemanticException;
import com.hopkins.simpledb.part2.catalog.Catalog;
import com.hopkins.simpledb.part2.catalog.KeyValueTable;

import javax.annotation.Nullable;

public class DeleteStatement implements ManipulationStatement {
  private final String tableName;
  @Nullable
  private final WhereClause whereClause;

  public DeleteStatement(String tableName, @Nullable WhereClause whereClause) {
    this.tableName = Preconditions.checkNotNull(tableName);
    this.whereClause = whereClause;
  }

  public String getTableName() {
    return tableName;
  }

  public boolean hasWhereClause() {
    return whereClause != null;
  }

  @Nullable
  public WhereClause getWhereClause() {
    return whereClause;
  }

  @Override
  public ManipulationResult execute(Catalog catalog) throws SemanticException {
    KeyValueTable table = catalog.getTable(tableName);
    if (hasWhereClause()) {
      if (!table.getSchema().hasColumn(whereClause.getColumnName())) {
        throw new SemanticException("Unknown column name: " + whereClause.getColumnName() + " in table: " + tableName);
      }
      return table.delete(whereClause.getLiteralValue());
    } else {
      return table.truncate();
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("DELETE FROM ").append(tableName);
    if (hasWhereClause()) {
      builder.append(whereClause);
    }
    return builder.toString();
  }
}
