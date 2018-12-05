package org.quasar.rpn;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.quasar.rpn.TokenFactory.givenCommandToken;
import static org.quasar.rpn.TokenFactory.givenInvalidInput;
import static org.quasar.rpn.TokenFactory.givenNumberToken;
import static org.quasar.rpn.TokenFactory.givenOperatorToken;
import static org.quasar.rpn.TokenFactory.givenToken;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quasar.rpn.tokens.CommandToken.Commands;
import org.quasar.rpn.tokens.OperatorToken.Operators;

public class CalculatorTest {
  private Calculator calc;

  @Before
  public void setUp() {
    calc = new Calculator();
  }

  @Test(expected = IllegalStateException.class)
  public void shouldThrowErrorWhenCalculatorCanNotProcessToken() {
    calc.push(givenToken());
  }

  @Test(expected = InsufficientOperatorParametersException.class)
  public void shouldThrowErrorWhenOperatorHasInsufficientParameters() {
    calc.push(givenOperatorToken(Operators.ADDITION));
  }

  @Test(expected = InvalidInputException.class)
  public void shouldThrowErrorWhenInvalidInputPushed() {
    calc.push(givenInvalidInput());
  }

  @Test
  public void shouldAcceptNumberToken() {
    final int value = 92;
    calc.push(givenNumberToken(value));

    assertThat(calc.getState(), hasItem(new BigDecimal(value)));
  }

  @Test
  public void shouldClearCalculator() {
    calc.push(givenNumberToken(12));
    calc.push(givenCommandToken(Commands.CLEAR));

    assertThat(calc.getState().size(), is(0));
  }

  @Test
  public void shouldUndoSimplePush() {
    calc.push(givenNumberToken(12));
    calc.push(givenNumberToken(23));
    calc.push(givenCommandToken(Commands.UNDO));

    assertThat(calc.getState().size(), is(1));

    assertThat(calc.getState(), hasItem(new BigDecimal(12)));
  }

  @Test
  public void shouldUndoCompositeOperations() {
    calc.push(givenNumberToken(5));
    calc.push(givenNumberToken(4));
    calc.push(givenOperatorToken(Operators.MULTIPLICATION));
    calc.push(givenNumberToken(5));
    calc.push(givenOperatorToken(Operators.MULTIPLICATION));
    calc.push(givenCommandToken(Commands.UNDO));

    final List<BigDecimal> state = calc.getState();
    assertThat(state.size(), is(2));

    assertThat(state.get(0), is(new BigDecimal(20)));
    assertThat(state.get(1), is(new BigDecimal(5)));
  }

  @Test
  public void shouldIgnoreUndoOnEmptyStack() {
    calc.push(givenCommandToken(Commands.UNDO));

    assertThat(calc.getState().size(), is(0));
  }

  @Test
  public void shouldUndoIllegalOperation() {
    calc.push(givenNumberToken(1));
    calc.push(givenNumberToken(0));

    try {
      calc.push(givenOperatorToken(Operators.DIVISION));

      fail("Calculator didn't throw the error");
    }
    catch (IllegalArithmeticOperationException e) {
      // we don't care about the error here, just that we got here.
    }
    finally {
      final List<BigDecimal> state = calc.getState();
      assertThat(state.size(), is(2));

      assertThat(state.get(0), is(new BigDecimal(1)));
      assertThat(state.get(1), is(new BigDecimal(0)));
    }
  }
}
