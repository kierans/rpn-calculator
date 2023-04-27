package org.quasar.rpn.operations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.quasar.rpn.IllegalArithmeticOperationException;
import org.quasar.rpn.tokens.OperatorToken;

public class BiOperandArithmeticOperation extends ArithmeticOperation {
  private final Operation a;
  private final Operation b;

  public BiOperandArithmeticOperation(final OperatorToken op, final Operation a, final Operation b) {
    super(op);

    this.a = a;
    this.b = b;
  }

  @Override
  public List<Operation> undo() {
    return Arrays.asList(a, b);
  }

  @Override
  public String asExpression() {
    return String.format("%s %s %s", a.getValue(), b.getValue(), this.op.input);
  }

  @Override
  protected BigDecimal doOperation(final OperatorToken token) {
    try {
      switch (token.op) {
        case ADDITION:
          return a.getValue().add(b.getValue());

        case SUBTRACTION:
          return a.getValue().subtract(b.getValue());

        case MULTIPLICATION:
          return a.getValue().multiply(b.getValue());

        case DIVISION:
          return BigDecimal.valueOf(a.getValue().doubleValue() / b.getValue().doubleValue());
      }
    }
    catch (Exception e) {
      throw new IllegalArithmeticOperationException(this, e);
    }

    throw new IllegalStateException(String.format("Can't operate on op %s", token));
  }
}
