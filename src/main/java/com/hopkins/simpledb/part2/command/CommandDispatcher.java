package com.hopkins.simpledb.part2.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hopkins.simpledb.part2.SemanticException;
import com.hopkins.simpledb.part2.SyntaxException;
import com.hopkins.simpledb.part2.catalog.Catalog;

import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandDispatcher {
  private static ImmutableList<Command> COMMAND_LIST =
      ImmutableList.of(
          new HelpCommand(),
          new QuitCommand(),
          new SchemaCommand(),
          new TablesCommand(),
          new VersionCommand());
  private static ImmutableMap<String, Command> COMMAND_MAP =
      ImmutableMap.copyOf(
          COMMAND_LIST
              .stream()
              .collect(Collectors.toMap(
                  Command::getName,
                  Function.identity())));

  public static Command getCommand(String command) {
    return COMMAND_MAP.get(command);
  }

  public static ImmutableList<Command> getCommandList() {
    return COMMAND_LIST;
  }

  public boolean isCommand(String line) {
    return line.startsWith(".");
  }

  public void handleCommand(String line, Catalog catalog) throws SyntaxException, SemanticException, QuitException {
    Preconditions.checkArgument(isCommand(line));

    String[] parts = line.split("\\s+");
    String commandText = parts[0].substring(1).toLowerCase();
    Command command = getCommand(commandText);
    if (command == null) {
      throw new SyntaxException("Unknown command: " + commandText);
    } else {
      String[] args = new String[parts.length - 1];
      System.arraycopy(parts, 1, args, 0, args.length);
      command.execute(catalog, args);
    }
  }
}
