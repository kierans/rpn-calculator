package org.quasar.rpn.tokens;

import lombok.ToString;

@ToString(callSuper = true)
public class InvalidInputToken extends Token {
  public InvalidInputToken(final String input, final Integer position) {
    super(input, position);
  }
}
