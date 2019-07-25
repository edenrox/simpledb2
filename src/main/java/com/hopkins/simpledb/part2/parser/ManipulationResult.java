package com.hopkins.simpledb.part2.parser;

import com.google.common.collect.ImmutableList;
import com.hopkins.simpledb.part2.catalog.Record;
import com.hopkins.simpledb.part2.catalog.Schema;
import com.hopkins.simpledb.part2.catalog.Column;

public class ManipulationResult {
  public interface Columns {
    String ROWS_AFFECTED = "rows_affected";
  }

  private static final Schema SCHEMA = new Schema(ImmutableList.of(new Column(Columns.ROWS_AFFECTED)));
  public static final ManipulationResult EMPTY = new ManipulationResult(0);
  public static final ManipulationResult SINGLE = new ManipulationResult(1);

  private int rowsAffected;

  public ManipulationResult(int rowsAffected) {
    this.rowsAffected = rowsAffected;
  }

  public int getRowsAffected() {
    return rowsAffected;
  }

  public Record getRecord() {
    return new Record(SCHEMA, new String[] {String.valueOf(rowsAffected)});
  }
}
