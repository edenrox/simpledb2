package com.hopkins.simpledb.part2.tokenizer;

public class Token {
  private final TokenType type;
  private final String lexeme;
  private final Object literal;

  Token(TokenType type, String lexeme, Object literal) {
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
  }

  public TokenType getType() {
    return type;
  }

  public String getLexeme() {
    return lexeme;
  }

  public Object getLiteral() {
    return literal;
  }

  public String toString() {
    if (type == TokenType.IDENTIFIER) {
      return type + ": " + lexeme;
    } else if (type == TokenType.STRING) {
      return type + ": " + literal;
    }
    return type.name();
  }
}
