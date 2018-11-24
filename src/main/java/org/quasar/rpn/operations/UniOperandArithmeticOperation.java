package org.quasar.rpn.operations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.quasar.rpn.tokens.OperatorToken;

public class UniOperandArithmeticOperation extends ArithmeticOperation {
  private final Operation operand;

  public UniOperandArithmeticOperation(final OperatorToken op, final Operation operand) {
    super(op);

    this.operand = operand;
  }

  @Override
  public List<Operation> undo() {
    return Collections.singletonList(operand);
  }

  @Override
  protected BigDecimal doOperation(final OperatorToken op) {
    /*
     * Currently we only have one uni operator.
     *
     * Math.sqrt should be sufficient without losing precision
     */
    return new BigDecimal(Math.sqrt(operand.getValue().doubleValue()));
  }
}
