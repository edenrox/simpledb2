package com.hopkins.simpledb.part2.command;

import com.hopkins.simpledb.part2.SemanticException;
import com.hopkins.simpledb.part2.catalog.Catalog;

public interface Command {

  String getName();

  String getDescription();

  void execute(Catalog catalog, String[] args) throws SemanticException, QuitException;
}
