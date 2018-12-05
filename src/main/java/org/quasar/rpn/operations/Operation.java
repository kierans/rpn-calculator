package org.quasar.rpn.operations;

import java.math.BigDecimal;
import java.util.List;

import org.quasar.rpn.Calculator;

/**
 * An Operation is a record of an operation performed on a {@link Calculator}.
 */
public interface Operation {
  /**
   * @return The result of the operation.
   */
  public BigDecimal getValue();

  /**
   * Undoing an operation is restoring the original parts back to the Calculator.
   *
   * @return Component parts of the operation.
   */
  public List<Operation> undo();

  /**
   * @return A string representation of the operation.
   */
  public String asExpression();
}
