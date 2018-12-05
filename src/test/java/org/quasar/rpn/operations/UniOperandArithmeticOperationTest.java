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

public class UniOperandArithmeticOperationTest {
  public static final int VALUE = 4;

  private UniOperandArithmeticOperation op;
  private Operation operand;
  private Operators operator;

  @Before
  public void setUp() {
    operand = givenNumberOperation(VALUE);
    operator = Operators.SQUARE_ROOT;

    op = new UniOperandArithmeticOperation(givenOperatorToken(operator), operand);
  }

  @Test
  public void shouldPerformSquareRoot() {
    op.computeValue();

    assertThat(op.getValue(), is(new BigDecimal(2)));
  }

  @Test
  public void shouldReturnOperandWhenUndoingOperation() {
    assertThat(op.undo(), hasItem(operand));
  }

  @Test
  public void shouldReturnOperationAsExpression() {
    assertThat(op.asExpression(), is(VALUE + " " + operator.token));
  }
}
