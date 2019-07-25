package com.hopkins.simpledb.part2.command;

import com.google.common.collect.ImmutableList;
import com.hopkins.simpledb.part2.SemanticException;
import com.hopkins.simpledb.part2.catalog.Catalog;
import com.hopkins.simpledb.part2.catalog.KeyValueTable;

import java.util.List;

public class SchemaCommand implements Command {
  @Override
  public String getName() {
    return "schema";
  }

  @Override
  public String getDescription() {
    return "Show the schema for objects in the catalog.";
  }

  @Override
  public void execute(Catalog catalog, String[] args) throws SemanticException {
    List<KeyValueTable> tableList;
    if (args.length > 0) {
      String tableName = args[0];
      KeyValueTable table = catalog.getTable(tableName);
      tableList = ImmutableList.of(table);
    } else {
      tableList = catalog.getTables();
    }
    System.out.println("Schema:");
    System.out.println("=======");
    for (KeyValueTable table : tableList) {
      System.out.println(table.getSchemaString());
    }
  }
}
