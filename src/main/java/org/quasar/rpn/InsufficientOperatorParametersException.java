package org.quasar.rpn;

import org.quasar.rpn.tokens.OperatorToken;

public class InsufficientOperatorParametersException extends RuntimeException {
  public final OperatorToken token;

  public InsufficientOperatorParametersException(final OperatorToken token) {
    super(String.format("Insufficient operands to operator %s", token.op));

    this.token = token;
  }
}
