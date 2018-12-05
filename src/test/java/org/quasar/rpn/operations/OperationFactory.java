package org.quasar.rpn.operations;

import java.math.BigDecimal;
import java.util.List;

public class OperationFactory {
  public static NumberOperation givenNumberOperation(int value) {
    return new NumberOperation(value);
  }

  private static class NumberOperation implements Operation {
    private final BigDecimal value;

    private NumberOperation(final int value) {
      this.value = new BigDecimal(value);
    }

    @Override
    public BigDecimal getValue() {
      return value;
    }

    @Override
    public List<Operation> undo() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String asExpression() {
      return value.toString();
    }
  }
}
