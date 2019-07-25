package com.hopkins.simpledb.part1;

import java.io.*;
import java.util.List;

public class SimpleDb {
  private static final String VERSION = "1.0 Basic REPL";

  private static final String COMMAND_QUIT = ".quit";
  private static final String COMMAND_VERSION = ".version";
  private static final String COMMAND_TABLES = ".tables";

  public static void main(String[] args) {
    boolean isDone = false;
    Catalog catalog = new Catalog();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      while (!isDone) {
        System.out.print("SimpleDB> ");
        String command = reader.readLine().trim();
        switch (command) {
          case COMMAND_QUIT:
            isDone = true;
            break;
          case COMMAND_VERSION:
            System.out.println("Simple DB");
            System.out.println("Version: " + VERSION);
            break;
          case COMMAND_TABLES:
            List<String> tables = catalog.getTableNames();
            for (String table : tables) {
              System.err.println(table);
            }
            break;
          default:
            System.out.println("Error: unrecognized command: " + command);
        }
      }
    } catch (IOException ex) {
      System.err.println("Error:\n" + ex);
    }
  }
}
