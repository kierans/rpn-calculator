package org.quasar.rpn;

import org.quasar.rpn.tokens.OperatorToken;

public class InsufficientOperatorParametersException extends RuntimeException {
  public final OperatorToken operatorToken;

  public InsufficientOperatorParametersException(final OperatorToken operatorToken) {
    super(String.format("Insufficient operands to operator %s", operatorToken.op));

    this.operatorToken = operatorToken;
  }
}
