package com.hopkins.simpledb.part2.parser;

import com.hopkins.simpledb.part2.ConstraintViolatedException;
import com.hopkins.simpledb.part2.SemanticException;
import com.hopkins.simpledb.part2.catalog.Catalog;

public interface ManipulationStatement {
  ManipulationResult execute(Catalog catalog) throws SemanticException, ConstraintViolatedException;
}
