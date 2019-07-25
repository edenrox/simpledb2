package com.hopkins.simpledb.part2.parser;

import com.google.common.base.Preconditions;
import com.hopkins.simpledb.part2.Operator;
import com.hopkins.simpledb.part2.SemanticException;
import com.hopkins.simpledb.part2.catalog.Catalog;
import com.hopkins.simpledb.part2.catalog.KeyValueTable;
import com.hopkins.simpledb.part2.operators.CountOperator;
import com.hopkins.simpledb.part2.operators.SelectOperator;

import javax.annotation.Nullable;

public class SelectStatement {
  private final boolean isCount;
  private final String tableName;
  private final WhereClause whereClause;

  public SelectStatement(boolean isCount, String tableName, @Nullable WhereClause whereClause) {
    this.isCount = isCount;
    this.tableName = Preconditions.checkNotNull(tableName);
    this.whereClause = whereClause;
  }

  public boolean isCount() {
    return isCount;
  }

  public String getTableName() {
    return tableName;
  }

  public boolean hasWhereClause() {
    return whereClause != null;
  }

  public WhereClause getWhereClause() {
    return whereClause;
  }

  public Operator execute(Catalog catalog) throws SemanticException {
    KeyValueTable table = catalog.getTable(tableName);
    Operator scan = getScan(table);
    if (isCount) {
      return new CountOperator(scan);
    } else {
      return scan;
    }
  }

  public Operator getScan(KeyValueTable table) throws SemanticException {
    if (!hasWhereClause()) {
      return table.scan();
    }
    if (!table.getSchema().hasColumn(whereClause.getColumnName())) {
      throw new SemanticException("Unknown column " + whereClause.getColumnName() + " in table " + table.getName());
    }
    if (whereClause.getColumnName().equalsIgnoreCase(KeyValueTable.Columns.KEY)) {
      return table.indexedScan(whereClause.getLiteralValue());
    }
    return new SelectOperator(table.scan(), whereClause);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    if (isCount) {
      builder.append("COUNT(*)");
    } else {
      builder.append("*");
    }
    builder.append(" FROM ").append(tableName);
    if (hasWhereClause()) {
      builder.append(whereClause);
    }
    return builder.toString();
  }
}
