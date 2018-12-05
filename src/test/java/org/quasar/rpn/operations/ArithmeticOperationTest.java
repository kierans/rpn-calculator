package org.quasar.rpn.operations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.quasar.rpn.TokenFactory.givenOperatorToken;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quasar.rpn.tokens.OperatorToken;
import org.quasar.rpn.tokens.OperatorToken.Operators;

public class ArithmeticOperationTest {
  private ArithmeticOperation op;

  @Before
  public void setUp() {
    op = new StubArithmeticOperation() ;
  }

  @Test(expected = IllegalStateException.class)
  public void shouldThrowErrorIfResultQueriedBeforeOperationPerformed() {
    op.getValue();
  }

  @Test
  public void shouldReturnComputedValue() {
    op.computeValue();

    assertThat(op.getValue(), is(new BigDecimal(0)));
  }

  private static class StubArithmeticOperation extends ArithmeticOperation {
    public StubArithmeticOperation() {
      super(givenOperatorToken(Operators.ADDITION));
    }

    @Override
    public List<Operation> undo() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String asExpression() {
      throw new UnsupportedOperationException();
    }

    @Override
    protected BigDecimal doOperation(final OperatorToken op) {
      return new BigDecimal(0);
    }
  }
}
