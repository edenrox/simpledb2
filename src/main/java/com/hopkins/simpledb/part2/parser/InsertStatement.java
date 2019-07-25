package com.hopkins.simpledb.part2.parser;

import com.google.common.base.Preconditions;
import com.hopkins.simpledb.part2.*;
import com.hopkins.simpledb.part2.catalog.Catalog;
import com.hopkins.simpledb.part2.catalog.KeyValueTable;
import com.hopkins.simpledb.part2.catalog.Record;

import java.io.IOException;

public class InsertStatement implements ManipulationStatement {
  private final String tableName;
  private final Record record;

  public InsertStatement(String tableName, Record record) {
    this.tableName = Preconditions.checkNotNull(tableName);
    this.record = Preconditions.checkNotNull(record);
  }

  public String getTableName() {
    return tableName;
  }

  public Record getRecord() {
    return record;
  }

  @Override
  public ManipulationResult execute(Catalog catalog) throws SemanticException, ConstraintViolatedException {
    KeyValueTable table = catalog.getTable(tableName);
    int statementNumColumns = record.getSchema().getColumns().size();
    int tableNumColumns = table.getSchema().getColumns().size();
    if (statementNumColumns != tableNumColumns) {
      throw new SemanticException("Expected " + tableNumColumns + " column(s), but found: " + statementNumColumns);
    }
    String key = record.getValue(0);
    try (Operator scan = table.indexedScan(key)) {
      if (scan.hasNext()) {
        throw new ConstraintViolatedException("Table " + tableName + " already contains key: " + key);
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    Record recordToInsert = new Record(table.getSchema(), record.getValues());
    return table.insert(recordToInsert);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO ").append(tableName);
    builder.append(" VALUES (");
    for (int i = 0; i < record.getSchema().getColumns().size(); i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append("'").append(record.getValue(i).replaceAll("'", "\\'")).append("'");
    }
    builder.append(")");
    return builder.toString();
  }
}
