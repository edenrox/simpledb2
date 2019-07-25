package com.hopkins.simpledb.part2.tokenizer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.function.Function;
import java.util.stream.Collectors;

public enum TokenType {
  LEFT_PAREN, RIGHT_PAREN,
  COMMA, DOT, STAR, EQUAL,

  IDENTIFIER,
  STRING,

  // Keywords
  COUNT,
  DELETE,
  FROM,
  INSERT,
  INTO,
  SELECT,
  SET,
  TRUNCATE,
  UPDATE,
  VALUES,
  WHERE,

  EOF;

  public static final ImmutableList<TokenType> KEYWORDS =
      ImmutableList.of(
          COUNT, DELETE, FROM, INSERT, INTO, SELECT, SET, TRUNCATE, UPDATE, VALUES, WHERE);
  public static final ImmutableMap<String, TokenType> KEYWORD_MAP =
      ImmutableMap.copyOf(
          KEYWORDS
              .stream()
              .collect(Collectors.toMap(
                  tokenType -> tokenType.name().toLowerCase(),
                  Function.identity())));
}
