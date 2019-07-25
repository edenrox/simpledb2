package com.hopkins.simpledb.part2.command;

import com.hopkins.simpledb.part2.catalog.Catalog;

public class VersionCommand implements Command {
  private static final String VERSION = "2.0 REPL + Single Table In-Memory Key-Value";

  @Override
  public String getName() {
    return "version";
  }

  @Override
  public String getDescription() {
    return "Prints the version of SimpleDB currently running.";
  }

  @Override
  public void execute(Catalog catalog, String[] args) {
    System.out.println("Version: " + VERSION);
  }
}
