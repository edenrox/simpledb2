package com.hopkins.simpledb.part2.parser;

import com.hopkins.simpledb.part2.SyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;

public class SimpleParser {
  private final String line;
  private int position;

  public SimpleParser(String line) {
    this.line = line.trim();
    this.position = 0;
  }

  public String getLine() {
    return line;
  }

  public int getPosition() {
    return position;
  }

  public boolean isAtEnd() {
    return position == line.length();
  }

  public void skipWhiteSpace() {
    while (position < line.length() && Character.isWhitespace(line.charAt(position))) {
      position++;
    }
  }

  public boolean peek(String token) {
    if (position + token.length() > line.length()) {
      return false;
    }
    if (!line.substring(position, position + token.length()).equalsIgnoreCase(token)) {
      return false;
    }
    return true;
  }

  public boolean accept(String token) {
    if (!peek(token)) {
      return false;
    }
    position += token.length();
    return true;
  }

  public void expect(String token) throws SyntaxException {
    int oldPosition = position;
    if (!accept(token)) {
      throw SyntaxException.newInstance("Expected " + token, line, oldPosition);
    }
  }

  public void expectEnd() throws SyntaxException {
    if (!isAtEnd()) {
      throw SyntaxException.newInstance("Expected end", line, position);
    }
  }

  public String readIdentifier() throws SyntaxException {
    if (isAtEnd()) {
      throw SyntaxException.newInstance("Unexpected end of line; expected identifier", line, position);
    }
    int startPosition = position;
    while (!isAtEnd()) {
      char c = line.charAt(position);
      if (Character.isLetter(c)) {
        position++;
        continue;
      }
      if (position > startPosition && Character.isDigit(c)) {
        position++;
        continue;
      }
      if (position == startPosition) {
        throw SyntaxException.newInstance("Expected identifier", line, startPosition);
      } else {
        break;
      }
    }
    return line.substring(startPosition, position);
  }

  public String readStringLiteral() throws SyntaxException {
    expect("'");
    int startPosition = position;
    while (line.charAt(position) != '\'') {
      position++;
      if (position >= line.length()) {
        throw SyntaxException.newInstance("Unterminated string literal", line, startPosition);
      }
    }
    position++;
    return line.substring(startPosition, position-1);
  }
}
