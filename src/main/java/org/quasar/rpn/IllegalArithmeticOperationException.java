package org.quasar.rpn;

import org.quasar.rpn.operations.Operation;

public class IllegalArithmeticOperationException extends RuntimeException {
  public IllegalArithmeticOperationException(final Operation operation, final Throwable cause) {
    super(String.format("'%s' is illegal arithmetic operation", operation.asExpression()), cause);
  }
}
