package com.hopkins.simpledb.part2.operators;

import com.google.common.collect.ImmutableList;
import com.hopkins.simpledb.part2.Operator;
import com.hopkins.simpledb.part2.catalog.Record;
import com.hopkins.simpledb.part2.catalog.Schema;
import com.hopkins.simpledb.part2.catalog.Column;

import java.io.IOException;

public final class CountOperator implements Operator {
  private static final String COLUMN_NAME = "cnt";
  private static final Schema SCHEMA = new Schema(ImmutableList.of(new Column(COLUMN_NAME)));

  private final Operator source;
  private boolean hasNext;

  public CountOperator(Operator source) {
    this.source = source;
    hasNext = true;
  }

  @Override
  public Schema getSchema() {
    return SCHEMA;
  }

  @Override
  public void reset() {
    source.reset();
    hasNext = true;
  }

  @Override
  public boolean hasNext() {
    return hasNext;
  }

  @Override
  public Record next() {
    int count = 0;
    while (source.hasNext()) {
      count++;
      source.next();
    }
    hasNext = false;
    return new Record(SCHEMA, new String[] {String.valueOf(count)});
  }

  @Override
  public void close() throws IOException {
    source.close();
  }
}
