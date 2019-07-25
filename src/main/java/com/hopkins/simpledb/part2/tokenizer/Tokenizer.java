package com.hopkins.simpledb.part2.tokenizer;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.hopkins.simpledb.part2.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
  private final String source;

  private int start;
  private int current;
  private List<Token> tokens;

  public Tokenizer(String source) {
    this.source = Preconditions.checkNotNull(source);
    this.tokens = new ArrayList<>();
  }

  public ImmutableList<Token> scanTokens() throws SyntaxException {
    while(!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(TokenType.EOF, "", null));
    return ImmutableList.copyOf(tokens);
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  private void scanToken() throws SyntaxException {
    char c = advance();
    switch (c) {
      case '(': addToken(TokenType.LEFT_PAREN); break;
      case ')': addToken(TokenType.RIGHT_PAREN); break;
      case ',': addToken(TokenType.COMMA); break;
      case '.': addToken(TokenType.DOT); break;
      case '*': addToken(TokenType.STAR); break;
      case '=': addToken(TokenType.EQUAL); break;
      case ' ':
      case '\r':
      case '\n':
      case '\t':
        // Ignore whitespace
        break;
      case '\'':
        string();
        break;
      default:
        if (isAlpha(c)) {
          identifier();
        } else {
          throw SyntaxException.newInstance("Unexpected character: " + c, source, current - 1);
        }
    }
  }

  private boolean isAllowedEscapeChar(char c) {
    return getEscapedChar(c) != '\0';
  }

  private char getEscapedChar(char c) {
    switch (c) {
      case '\\':
      case '\'':
        return c;
      case 'n':
        return '\n';
      case 't':
        return '\t';
      case 'r':
        return '\r';
      default:
        return '\0';
    }
  }

  private void string() throws SyntaxException {
    StringBuilder builder = new StringBuilder();
    while (peek() != '\'' && !isAtEnd()) {
      // Skip the \ of an escaped character
      if (peek() == '\\') {
        if (isAllowedEscapeChar(peekNext())) {
          builder.append(getEscapedChar(peekNext()));
          advance();
          advance();
        } else {
          throw SyntaxException.newInstance("Invalid escape: " + peek() + peekNext(), source, current);
        }
      } else {
        builder.append(peek());
        advance();
      }
    }

    // Unterminated string.
    if (isAtEnd()) {
      throw SyntaxException.newInstance("Unterminated string", source, current);
    }

    // The closing '.
    advance();

    addToken(TokenType.STRING, builder.toString());
  }

  private void identifier() {
    while (isAlphaNumeric(peek())) {
      advance();
    }

    String text = source.substring(start, current);
    TokenType type = TokenType.KEYWORD_MAP.getOrDefault(text.toLowerCase(), TokenType.IDENTIFIER);
    addToken(type);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal));
  }

  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }

  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }

  private char advance() {
    current++;
    return source.charAt(current - 1);
  }

  private static boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
        (c >= 'A' && c <= 'Z') ||
        c == '_';
  }

  private static boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  private static boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }
}
