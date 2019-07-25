package com.hopkins.simpledb.part2.operators;

import com.google.common.base.Preconditions;
import com.hopkins.simpledb.part2.Operator;
import com.hopkins.simpledb.part2.parser.WhereClause;
import com.hopkins.simpledb.part2.catalog.Record;
import com.hopkins.simpledb.part2.catalog.Schema;

import java.io.IOException;
import java.util.NoSuchElementException;

public class SelectOperator implements Operator {
  private final Operator source;
  private final WhereClause whereClause;
  private Record next;

  public SelectOperator(Operator source, WhereClause whereClause) {
    this.source = Preconditions.checkNotNull(source);
    this.whereClause = Preconditions.checkNotNull(whereClause);
  }

  @Override
  public Schema getSchema() {
    return source.getSchema();
  }

  @Override
  public void reset() {
    source.reset();
  }

  private void moveToNext() {
    while (source.hasNext()) {
      Record record = source.next();
      if (matches(record)) {
        next = record;
        return;
      }
    }
    next = null;
  }

  private boolean matches(Record record) {
    return record.getValue(whereClause.getColumnName()).equals(whereClause.getLiteralValue());
  }

  @Override
  public boolean hasNext() {
    if (next == null) {
      moveToNext();
    }
    return next != null;
  }

  @Override
  public Record next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    Record value = next;
    moveToNext();
    return value;
  }

  @Override
  public void close() throws IOException {
    source.close();
  }
}
