package com.hopkins.simpledb.part2.catalog;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.hopkins.simpledb.part2.*;
import com.hopkins.simpledb.part2.operators.EmptyRowOperator;
import com.hopkins.simpledb.part2.operators.SingleRowOperator;
import com.hopkins.simpledb.part2.parser.ManipulationResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class KeyValueTable {
  public interface Columns {
    String KEY = "key";
    String VALUE = "value";
  }

  private final String name;
  private final Schema schema;
  private final HashMap<String, String> data;

  public KeyValueTable(String name) {
    this.name = name;
    this.schema = new Schema(ImmutableList.of(new Column(Columns.KEY), new Column(Columns.VALUE)));
    this.data = new HashMap<>();
  }

  public String getName() {
    return name;
  }

  public Schema getSchema() {
    return schema;
  }

  public String getSchemaString() {
    StringBuilder builder = new StringBuilder();
    builder.append("CREATE TABLE ").append(name).append(" (");
    for (Column column : schema.getColumns()) {
      builder.append(column.getName()).append(", ").append(column.getType()).append(",");
    }
    builder.setLength(builder.length() - 1);
    builder.append(");");
    return builder.toString();
  }

  public ManipulationResult insert(Record record) throws ConstraintViolatedException {
    Preconditions.checkArgument(record.getSchema().equals(schema));
    String key = record.getValue(Columns.KEY);
    if (data.containsKey(key)) {
      throw new ConstraintViolatedException("Unique constraint failed: " + name + "." + Columns.KEY);
    }
    String value = record.getValue(Columns.VALUE);
    data.put(key, value);
    return ManipulationResult.SINGLE;
  }

  private Record buildUpdatedRecord(String key, List<ColumnValuePair> pairs) {
    // Populate the existing values
    HashMap<String, String> map = new HashMap<>();
    map.put(Columns.KEY, key);
    map.put(Columns.VALUE, data.get(key));

    // Update with assignments
    for (ColumnValuePair pair : pairs) {
      // Normalize the column names
      String columnName = null;
      if (Columns.KEY.equalsIgnoreCase(pair.getColumnName())) {
        columnName = Columns.KEY;
      } else {
        columnName = Columns.VALUE;
      }
      map.put(columnName, pair.getLiteralValue());
    }

    // Return a Record
    return new Record(schema, new String[] {map.get(Columns.KEY), map.get(Columns.VALUE)});
  }

  public ManipulationResult update(String key, List<ColumnValuePair> pairs)
      throws ConstraintViolatedException {
    if (data.containsKey(key)) {
      Record updatedRecord = buildUpdatedRecord(key, pairs);
      String newKey = updatedRecord.getValue(Columns.KEY);
      if (!newKey.equals(key)) {
        if (data.containsKey(newKey)) {
          throw new ConstraintViolatedException("Unique constraint failed: " + name + "." + Columns.KEY);
        }
        data.remove(key);
      }
      data.put(newKey, updatedRecord.getValue(Columns.VALUE));
      return ManipulationResult.SINGLE;
    } else {
      return ManipulationResult.EMPTY;
    }
  }

  public ManipulationResult truncate() {
    int rowsAffected = data.size();
    data.clear();
    return new ManipulationResult(rowsAffected);
  }

  public ManipulationResult delete(String key) {
    if (!data.containsKey(key)) {
      return ManipulationResult.EMPTY;
    }
    data.remove(key);
    return ManipulationResult.SINGLE;
  }

  /** Scan all rows in the table. */
  public Operator scan() {
    return new ScanOperator();
  }

  public Operator indexedScan(String key) {
    if (data.containsKey(key)) {
      String value = data.get(key);
      return new SingleRowOperator(new Record(schema, new String[] {key, value}));
    } else {
      return new EmptyRowOperator(schema);
    }
  }

  private final class ScanOperator implements Operator {
    private Iterator<String> keyIterator;

    ScanOperator() {
      reset();
    }

    @Override
    public Schema getSchema() {
      return schema;
    }

    @Override
    public void reset() {
      keyIterator = data.keySet().iterator();
    }

    @Override
    public boolean hasNext() {
      return keyIterator.hasNext();
    }

    @Override
    public Record next() {
      String key = keyIterator.next();
      String value = data.get(key);

      return new Record(schema, new String[] { key, value});
    }

    @Override
    public void close() {}
  }
}
