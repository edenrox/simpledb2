package com.hopkins.simpledb.part2.parser;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.hopkins.simpledb.part2.SyntaxException;
import com.hopkins.simpledb.part2.catalog.Column;
import com.hopkins.simpledb.part2.catalog.ColumnValuePair;
import com.hopkins.simpledb.part2.catalog.Record;
import com.hopkins.simpledb.part2.catalog.Schema;
import com.hopkins.simpledb.part2.tokenizer.Token;
import com.hopkins.simpledb.part2.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {
  private final List<Token> tokenList;
  private int current;

  public SqlParser(List<Token> tokenList) {
    this.tokenList = Preconditions.checkNotNull(tokenList);
    this.current = 0;
  }

  private Token currentToken() {
    return tokenList.get(current);
  }

  private TokenType peek() {
    return currentToken().getType();
  }

  private boolean accept(TokenType type) {
    if (peek() == type) {
      current++;
      return true;
    }
    return false;
  }

  private void expect(TokenType type) throws SyntaxException {
    if (peek() == type) {
      current++;
    } else {
      throw new SyntaxException("Unexpected token: " + currentToken());
    }
  }

  private String expectIdentifier() throws SyntaxException {
    if (peek() != TokenType.IDENTIFIER) {
      throw new SyntaxException("Unexpected token: " + currentToken());
    }
    String identifier = tokenList.get(current).getLexeme();
    current++;
    return identifier;
  }

  private String expectStringLiteral() throws SyntaxException {
    if (peek() != TokenType.STRING) {
      throw new SyntaxException("Unexpected token: " + currentToken());
    }
    String literal = (String) tokenList.get(current).getLiteral();
    current++;
    return literal;
  }

  public boolean isSelect() {
    return peek() == TokenType.SELECT;
  }

  public ManipulationStatement parseManipulationStatement() throws SyntaxException {
    switch (peek()) {
      case INSERT:
        return parseInsert();
      case DELETE:
        return parseDelete();
      case TRUNCATE:
        return parseTruncate();
      case UPDATE:
        return parseUpdate();
      default:
        throw new SyntaxException("Unexpected statement: " + currentToken());
    }
  }

  DeleteStatement parseTruncate() throws SyntaxException {
    expect(TokenType.TRUNCATE);
    String tableName = expectIdentifier();
    expect(TokenType.EOF);
    return new DeleteStatement(tableName, /* whereClause= */ null);
  }

  DeleteStatement parseDelete() throws SyntaxException {
    expect(TokenType.DELETE);
    expect(TokenType.FROM);
    String tableName = expectIdentifier();
    WhereClause whereClause = null;
    if (peek() == TokenType.WHERE) {
      whereClause = parseWhereClause();
    }
    expect(TokenType.EOF);
    return new DeleteStatement(tableName, whereClause);
  }

  UpdateStatement parseUpdate() throws SyntaxException {
    expect(TokenType.UPDATE);
    String tableName = expectIdentifier();
    expect(TokenType.SET);
    AssignmentClause assignmentClause = parseAssignmentClause();
    WhereClause whereClause = null;
    if (peek() == TokenType.WHERE) {
      whereClause = parseWhereClause();
    }
    return new UpdateStatement(tableName, assignmentClause, whereClause);
  }

  AssignmentClause parseAssignmentClause() throws SyntaxException {
    ImmutableList.Builder<ColumnValuePair> listBuilder = ImmutableList.builder();
    while (true) {
      String columnName = expectIdentifier();
      expect(TokenType.EQUAL);
      String literalValue = expectStringLiteral();
      listBuilder.add(new ColumnValuePair(columnName, literalValue));
      if (!accept(TokenType.COMMA)) {
        break;
      }
    }
    return new AssignmentClause(listBuilder.build());
  }

  InsertStatement parseInsert() throws SyntaxException {
    expect(TokenType.INSERT);
    expect(TokenType.INTO);
    String tableName = expectIdentifier();
    expect(TokenType.VALUES);
    expect(TokenType.LEFT_PAREN);
    List<String> literals = new ArrayList<>();
    List<Column> columns = new ArrayList<>();
    while (true) {
      literals.add(expectStringLiteral());
      columns.add(new Column(""));
      if (accept(TokenType.RIGHT_PAREN)) {
        break;
      } else if (!accept(TokenType.COMMA)) {
        throw new SyntaxException("Expected ',' or ')', but was: " + currentToken());
      }
    }
    expect(TokenType.EOF);
    Schema schema = new Schema(columns);
    Record record = new Record(schema, literals.toArray(new String[0]));
    return new InsertStatement(tableName, record);
  }

  public SelectStatement parseSelect() throws SyntaxException {
    expect(TokenType.SELECT);
    boolean isCount;
    if (accept(TokenType.STAR)) {
      isCount = false;
    } else {
      expect(TokenType.COUNT);
      expect(TokenType.LEFT_PAREN);
      expect(TokenType.STAR);
      expect(TokenType.RIGHT_PAREN);
      isCount = true;
    }
    expect(TokenType.FROM);
    String tableName = expectIdentifier();
    WhereClause whereClause = null;
    if (peek() == TokenType.WHERE) {
      whereClause = parseWhereClause();
    }
    expect(TokenType.EOF);
    return new SelectStatement(isCount, tableName, whereClause);
  }

  WhereClause parseWhereClause() throws SyntaxException {
    expect(TokenType.WHERE);
    String columnName = expectIdentifier();
    expect(TokenType.EQUAL);
    String literalValue = expectStringLiteral();
    return new WhereClause(columnName, literalValue);
  }
}
