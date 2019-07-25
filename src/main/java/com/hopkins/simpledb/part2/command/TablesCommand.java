package com.hopkins.simpledb.part2.command;

import com.hopkins.simpledb.part2.catalog.Catalog;
import com.hopkins.simpledb.part2.catalog.KeyValueTable;

public class TablesCommand implements Command {
  @Override
  public String getName() {
    return "tables";
  }

  @Override
  public String getDescription() {
    return "List objects in the catalog";
  }

  @Override
  public void execute(Catalog catalog, String[] args) {
    System.out.println("Tables:");
    System.out.println("=======");
    for (KeyValueTable table : catalog.getTables()) {
      System.out.println(table.getName());
    }
  }
}
