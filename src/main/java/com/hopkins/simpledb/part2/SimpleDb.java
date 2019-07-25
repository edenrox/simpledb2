package com.hopkins.simpledb.part2;

import com.hopkins.simpledb.part2.catalog.*;
import com.hopkins.simpledb.part2.command.CommandDispatcher;
import com.hopkins.simpledb.part2.command.QuitException;
import com.hopkins.simpledb.part2.parser.*;
import com.hopkins.simpledb.part2.tokenizer.Token;
import com.hopkins.simpledb.part2.tokenizer.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleDb {
  private static Catalog catalog;

  public static void main(String[] args) throws Exception {
    catalog = new Catalog();
    CommandDispatcher commandDispatcher = new CommandDispatcher();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      while (true) {
        System.out.print("SimpleDB> ");
        String line = reader.readLine().trim();
        if (line.isEmpty()) {
          continue;
        }
        try {
          if (commandDispatcher.isCommand(line)) {
            commandDispatcher.handleCommand(line, catalog);
          } else {
            Tokenizer tokenizer = new Tokenizer(line);
            List<Token> tokens = tokenizer.scanTokens();
            System.out.println("[DEBUG] tokens: " + tokens);
            SqlParser sqlParser = new SqlParser(tokens);
            if (sqlParser.isSelect()) {
              SelectStatement selectStatement = sqlParser.parseSelect();
              System.out.println("[DEBUG] parsed: " + selectStatement);
              print(selectStatement.execute(catalog));
            } else {
              ManipulationStatement manipulationStatement = sqlParser.parseManipulationStatement();
              System.out.println("[DEBUG] parsed: " + manipulationStatement);
              print(manipulationStatement.execute(catalog));
            }
          }
        } catch (SyntaxException ex) {
          System.out.println("Syntax Error: " + ex.getMessage());
          ex.printStackTrace();
        } catch (SemanticException ex) {
          System.out.println("Semantic Error: " + ex.getMessage());
          ex.printStackTrace();
        } catch (ConstraintViolatedException ex) {
          System.out.println("Constraint Violated: " + ex.getMessage());
          ex.printStackTrace();
        }
      }
    } catch (QuitException ex) {
      System.exit(0);
    } catch (IOException ex) {
      System.err.println("Error:\n" + ex);
      ex.printStackTrace();
      System.exit(65);
    }
  }

  private static void print(ManipulationResult result) {
    System.out.println("Rows affected: " + result.getRowsAffected());
  }

  private static void print(Operator result) {
    String schemaHeader = getSchemaHeader(result.getSchema());
    System.out.println(schemaHeader);
    for (int i = 0; i < schemaHeader.length(); i++) {
      System.out.print("=");
    }
    System.out.println();
    while (result.hasNext()) {
      printRecord(result.next());
    }
  }

  private static String getSchemaHeader(Schema schema) {
    StringBuilder builder = new StringBuilder();
    for (Column column : schema.getColumns()) {
      builder.append(column.getName()).append(" | ");
    }
    builder.setLength(builder.length() - 3);
    return builder.toString();
  }

  private static void printRecord(Record record) {
    for (int i = 0; i < record.getSchema().getColumns().size(); i++) {
      if (i > 0) {
        System.out.print(" | ");
      }
      System.out.print(record.getValue(i));
    }
    System.out.print("\n");
  }
}
