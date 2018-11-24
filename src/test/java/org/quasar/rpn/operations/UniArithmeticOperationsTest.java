package org.quasar.rpn.operations;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.quasar.rpn.TokenFactory.givenOperatorToken;
import static org.quasar.rpn.operations.OperationFactory.givenNumberOperation;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.quasar.rpn.tokens.OperatorToken.Operators;

public class UniArithmeticOperationsTest {
  private UniOperandArithmeticOperation op;
  private Operation operand;

  @Before
  public void setUp() {
    operand = givenNumberOperation(4);
    op = new UniOperandArithmeticOperation(givenOperatorToken(Operators.SQUARE_ROOT), operand);
  }

  @Test
  public void shouldPerformSquareRoot() {
    assertThat(op.getValue(), is(new BigDecimal(2)));
  }

  @Test
  public void testShouldReturnOperandWhenUndoingOperation() {
    assertThat(op.undo(), hasItem(operand));
  }
}
