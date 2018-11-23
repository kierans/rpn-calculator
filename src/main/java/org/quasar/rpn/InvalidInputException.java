package org.quasar.rpn;

import org.quasar.rpn.tokens.InvalidInputToken;

public class InvalidInputException extends RuntimeException {
  public final InvalidInputToken token;

  public InvalidInputException(final InvalidInputToken token) {
    super(String.format("Invalid input: %s", token.input));

    this.token = token;
  }
}
