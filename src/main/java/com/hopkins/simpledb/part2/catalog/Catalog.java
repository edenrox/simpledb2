package com.hopkins.simpledb.part2.catalog;

import com.google.common.collect.ImmutableList;
import com.hopkins.simpledb.part2.SemanticException;

import java.util.List;

public class Catalog {
  public static final String SINGLE_TABLE_NAME = "main";
  private final List<KeyValueTable> tables;

  public Catalog() {
    tables = ImmutableList.of(new KeyValueTable(SINGLE_TABLE_NAME));
  }

  public List<KeyValueTable> getTables() {
    return tables;
  }

  public KeyValueTable getTable(String name) throws SemanticException {
    for (KeyValueTable table : tables) {
      if (table.getName().equalsIgnoreCase(name)) {
        return table;
      }
    }
    throw new SemanticException("Unknown table: " + name);
  }
}
