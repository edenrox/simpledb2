package com.hopkins.simpledb.part2.catalog;

import com.google.common.base.Preconditions;

public class Record {
  private final Schema schema;
  private final String[] values;

  public Record(Schema schema, String[] values) {
    this.schema = Preconditions.checkNotNull(schema);
    this.values = Preconditions.checkNotNull(values);
    Preconditions.checkArgument(schema.getColumns().size() == values.length);
  }

  public Schema getSchema() {
    return schema;
  }

  public String getValue(String columnName) {
    return getValue(schema.indexOf(columnName));
  }

  public String getValue(int index) {
    Preconditions.checkElementIndex(index, values.length);
    return values[index];
  }

  public String[] getValues() {
    return values;
  }
}
