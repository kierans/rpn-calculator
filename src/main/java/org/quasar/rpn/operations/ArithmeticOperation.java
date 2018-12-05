package org.quasar.rpn.operations;

import java.math.BigDecimal;

import org.quasar.rpn.tokens.OperatorToken;

public abstract class ArithmeticOperation implements Operation {
  public final OperatorToken op;

  private BigDecimal result;

  public ArithmeticOperation(final OperatorToken op) {
    this.op = op;
  }

  @Override
  public BigDecimal getValue() {
    if (result == null) {
      throw new IllegalStateException("Can't return value before computing it");
    }

    return result;
  }

  public void computeValue() {
    result = doOperation(op);
  }

  protected abstract BigDecimal doOperation(final OperatorToken op);
}
