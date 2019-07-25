package com.hopkins.simpledb.part2.parser;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.hopkins.simpledb.part2.ConstraintViolatedException;
import com.hopkins.simpledb.part2.Operator;
import com.hopkins.simpledb.part2.SemanticException;
import com.hopkins.simpledb.part2.catalog.Catalog;
import com.hopkins.simpledb.part2.catalog.ColumnValuePair;
import com.hopkins.simpledb.part2.catalog.KeyValueTable;
import com.hopkins.simpledb.part2.catalog.Record;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdateStatement implements ManipulationStatement {
  private final String tableName;
  private final AssignmentClause assignmentClause;
  @Nullable
  private final WhereClause whereClause;

  public UpdateStatement(String tableName, AssignmentClause assignmentClause, @Nullable WhereClause whereClause) {
    this.tableName = Preconditions.checkNotNull(tableName);
    this.assignmentClause = Preconditions.checkNotNull(assignmentClause);
    this.whereClause = whereClause;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("UPDATE ").append(tableName).append(" SET ").append(assignmentClause);
    if (whereClause != null) {
      builder.append(whereClause);
    }
    return builder.toString();
  }

  @Override
  public ManipulationResult execute(Catalog catalog) throws SemanticException, ConstraintViolatedException {
    KeyValueTable table = catalog.getTable(tableName);

    // Validate the assignment clause
    for (ColumnValuePair pair : assignmentClause.getColumnValuePairList()) {
      if (!table.getSchema().hasColumn(pair.getColumnName())) {
        throw new SemanticException("Unknown column: " + table.getName() + "." + pair.getColumnName());
      }
    }

    // Find the keys to update
    List<String> keysToUpdate = new ArrayList<>();
    SelectStatement selectStatement = new SelectStatement(false, tableName, whereClause);
    try (Operator selectOperator = selectStatement.execute(catalog)) {
      while (selectOperator.hasNext()) {
        Record record = selectOperator.next();
        keysToUpdate.add(record.getValue(KeyValueTable.Columns.KEY));
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    if (keysToUpdate.size() > 1 && assignmentClause.containsColumn(KeyValueTable.Columns.KEY)) {
      throw new ConstraintViolatedException(
          "Cannot UPDATE multiple rows of " + tableName + "." + KeyValueTable.Columns.KEY + " to the same value");
    }

    // Update the keys
    int rowsAffected = 0;
    for (String key : keysToUpdate) {
      ManipulationResult result = table.update(key, assignmentClause.getColumnValuePairList());
      rowsAffected += result.getRowsAffected();
    }
    return new ManipulationResult(rowsAffected);
  }
}
