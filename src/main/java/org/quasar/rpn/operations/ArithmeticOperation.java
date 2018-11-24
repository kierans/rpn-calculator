package org.quasar.rpn.operations;

import java.math.BigDecimal;

import org.quasar.rpn.tokens.OperatorToken;

public abstract class ArithmeticOperation implements Operation {
  public final OperatorToken op;
  public BigDecimal result;

  public ArithmeticOperation(final OperatorToken op) {
    this.op = op;
  }

  @Override
  public BigDecimal getValue() {
    if (result == null) {
      result = doOperation(op);
    }

    return result;
  }

  protected abstract BigDecimal doOperation(final OperatorToken op);
}
