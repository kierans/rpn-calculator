package org.quasar.rpn.operations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.quasar.rpn.TokenFactory.givenOperatorToken;
import static org.quasar.rpn.operations.OperationFactory.givenNumberOperation;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.quasar.rpn.tokens.OperatorToken.Operators;

public class BiOperandArithmeticOperationsTest {
  private ArithmeticOperation op;

  @Test(expected = IllegalStateException.class)
  public void shouldThrowErrorWhenOperatorTokenNotRecognised() {
    op = new BiOperandArithmeticOperation(givenOperatorToken(Operators.SQUARE_ROOT), givenNumberOperation(1),
        givenNumberOperation(1));

    op.getValue();
  }

  @Test
  public void shouldReturnOperationAsExpression() {
    final Operators operator = Operators.ADDITION;
    final int a = 1;
    final int b = 2;

    op = new BiOperandArithmeticOperation(givenOperatorToken(operator), givenNumberOperation(a),
      givenNumberOperation(b));

    assertThat(op.asExpression(), is(String.format("%d %d %s", a, b, operator.token)));
  }

  @Test
  public void shouldPerformAddition() {
    op = new BiOperandArithmeticOperation(givenOperatorToken(Operators.ADDITION), givenNumberOperation(1),
        givenNumberOperation(2));

    assertThat(op.getValue(), is(new BigDecimal(3)));
  }

  @Test
  public void shouldPerformSubtraction() {
    op = new BiOperandArithmeticOperation(givenOperatorToken(Operators.SUBTRACTION), givenNumberOperation(2),
        givenNumberOperation(1));

    assertThat(op.getValue(), is(new BigDecimal(1)));
  }

  @Test
  public void shouldPerformMultiplication() {
    op = new BiOperandArithmeticOperation(givenOperatorToken(Operators.MULTIPLICATION), givenNumberOperation(1),
        givenNumberOperation(2));

    assertThat(op.getValue(), is(new BigDecimal(2)));
  }

  @Test
  public void shouldPerformDivision() {
    op = new BiOperandArithmeticOperation(givenOperatorToken(Operators.DIVISION), givenNumberOperation(42),
        givenNumberOperation(4));

    assertThat(op.getValue(), is(new BigDecimal(10.5)));
  }

  @Test
  public void shouldReturnOperandsWhenUndoingOperation() {
    final Operation a = givenNumberOperation(42);
    final Operation b = givenNumberOperation(4);

    op = new BiOperandArithmeticOperation(givenOperatorToken(Operators.DIVISION), a, b);

    final List<Operation> ops = op.undo();

    assertThat(ops.size(), is(2));

    // order is important
    assertThat(ops.get(0), is(a));
    assertThat(ops.get(1), is(b));
  }
}
