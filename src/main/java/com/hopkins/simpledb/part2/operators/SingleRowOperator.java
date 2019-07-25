package com.hopkins.simpledb.part2.operators;

import com.hopkins.simpledb.part2.Operator;
import com.hopkins.simpledb.part2.catalog.Record;
import com.hopkins.simpledb.part2.catalog.Schema;

import java.util.NoSuchElementException;

public final class SingleRowOperator implements Operator {
  private final Record record;
  private boolean hasNext;

  public SingleRowOperator(Record record) {
    this.record = record;
    this.hasNext = true;
  }

  @Override
  public Schema getSchema() {
    return record.getSchema();
  }

  @Override
  public void reset() {
    hasNext = true;
  }

  @Override
  public boolean hasNext() {
    return hasNext;
  }

  @Override
  public Record next() {
    if (!hasNext) {
      throw new NoSuchElementException();
    }
    hasNext = false;
    return record;
  }

  @Override
  public void close() {}
}
