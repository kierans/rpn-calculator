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

public class MemoryTest {
  private Memory memory;

  @Before
  public void setUp() {
    memory = new Memory();
  }

  @Test(expected = IllegalStateException.class)
  public void shouldThrowErrorWhenCalculatorCanNotProcessToken() {
    memory.push(givenToken());
  }

  @Test(expected = InsufficientOperatorParametersException.class)
  public void shouldThrowErrorWhenOperatorHasInsufficientParameters() {
    memory.push(givenOperatorToken(Operators.ADDITION));
  }

  @Test(expected = InvalidInputException.class)
  public void shouldThrowErrorWhenInvalidInputPushed() {
    memory.push(givenInvalidInput());
  }

  @Test
  public void shouldAcceptNumberToken() {
    final int value = 92;
    memory.push(givenNumberToken(value));

    assertThat(memory.getState(), hasItem(new BigDecimal(value)));
  }

  @Test
  public void shouldClearCalculator() {
    memory.push(givenNumberToken(12));
    memory.push(givenCommandToken(Commands.CLEAR));

    assertThat(memory.getState().size(), is(0));
  }

  @Test
  public void shouldUndoSimplePush() {
    memory.push(givenNumberToken(12));
    memory.push(givenNumberToken(23));
    memory.push(givenCommandToken(Commands.UNDO));

    assertThat(memory.getState().size(), is(1));

    assertThat(memory.getState(), hasItem(new BigDecimal(12)));
  }

  @Test
  public void shouldUndoCompositeOperations() {
    memory.push(givenNumberToken(5));
    memory.push(givenNumberToken(4));
    memory.push(givenOperatorToken(Operators.MULTIPLICATION));
    memory.push(givenNumberToken(5));
    memory.push(givenOperatorToken(Operators.MULTIPLICATION));
    memory.push(givenCommandToken(Commands.UNDO));

    final List<BigDecimal> state = memory.getState();
    assertThat(state.size(), is(2));

    assertThat(state.get(0), is(new BigDecimal(20)));
    assertThat(state.get(1), is(new BigDecimal(5)));
  }

  @Test
  public void shouldIgnoreUndoOnEmptyStack() {
    memory.push(givenCommandToken(Commands.UNDO));

    assertThat(memory.getState().size(), is(0));
  }

  @Test
  public void shouldUndoIllegalOperation() {
    memory.push(givenNumberToken(1));
    memory.push(givenNumberToken(0));

    try {
      memory.push(givenOperatorToken(Operators.DIVISION));

      fail("Calculator didn't throw the error");
    }
    catch (IllegalArithmeticOperationException e) {
      // we don't care about the error here, just that we got here.
    }
    finally {
      final List<BigDecimal> state = memory.getState();
      assertThat(state.size(), is(2));

      assertThat(state.get(0), is(new BigDecimal(1)));
      assertThat(state.get(1), is(new BigDecimal(0)));
    }
  }
}
