package com.hopkins.simpledb.part2.operators;

import com.hopkins.simpledb.part2.Operator;
import com.hopkins.simpledb.part2.catalog.Record;
import com.hopkins.simpledb.part2.catalog.Schema;

import java.util.NoSuchElementException;

public class EmptyRowOperator implements Operator {

  private final Schema schema;

  public EmptyRowOperator(Schema schema) {
    this.schema = schema;
  }

  @Override
  public Schema getSchema() {
    return schema;
  }

  @Override
  public void reset() { }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public Record next() {
    throw new NoSuchElementException();
  }

  @Override
  public void close() {}
}
