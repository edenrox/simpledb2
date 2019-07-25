package com.hopkins.simpledb.part2.command;

import com.hopkins.simpledb.part2.SemanticException;
import com.hopkins.simpledb.part2.catalog.Catalog;

public class QuitCommand implements Command {
  @Override
  public String getName() {
    return "quit";
  }

  @Override
  public String getDescription() {
    return "Quit the read evaluate print loop (REPL).";
  }

  @Override
  public void execute(Catalog catalog, String[] args) throws QuitException {
    throw new QuitException();
  }
}
