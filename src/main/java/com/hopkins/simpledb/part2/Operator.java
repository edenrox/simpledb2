package com.hopkins.simpledb.part2;

import com.hopkins.simpledb.part2.catalog.Record;
import com.hopkins.simpledb.part2.catalog.Schema;

import java.io.Closeable;

public interface Operator extends Closeable {
  Schema getSchema();

  void reset();

  boolean hasNext();

  Record next();
}
