package com.hopkins.simpledb.part2.command;

import com.hopkins.simpledb.part2.catalog.Catalog;

public class HelpCommand implements Command {
  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "Show this message.";
  }

  @Override
  public void execute(Catalog catalog, String[] args) {
    System.out.println("Help");
    System.out.println("====");
    System.out.println("Commands:");
    for (Command command : CommandDispatcher.getCommandList()) {
      System.out.print(".");
      System.out.print(String.format("%20s", command.getName()));
      System.out.println(command.getDescription());
    }
  }
}
