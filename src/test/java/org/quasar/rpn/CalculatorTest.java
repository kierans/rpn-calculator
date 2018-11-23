package org.quasar.rpn;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.quasar.rpn.tokens.CommandToken;
import org.quasar.rpn.tokens.CommandToken.Commands;
import org.quasar.rpn.tokens.InvalidInputToken;
import org.quasar.rpn.tokens.NumberToken;
import org.quasar.rpn.tokens.OperatorToken;
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
  public void shouldPerformSquareRoot() {
    calc.push(givenNumberToken(4));
    calc.push(givenOperatorToken(Operators.SQUARE_ROOT));

    assertThat(calc.getState(), hasItem(new BigDecimal(2)));
  }

  @Test
  public void shouldPerformAddition() {
    calc.push(givenNumberToken(1));
    calc.push(givenNumberToken(2));
    calc.push(givenOperatorToken(Operators.ADDITION));

    assertThat(calc.getState(), hasItem(new BigDecimal(3)));
  }

  @Test
  public void shouldPerformSubtraction() {
    calc.push(givenNumberToken(2));
    calc.push(givenNumberToken(1));
    calc.push(givenOperatorToken(Operators.SUBTRACTION));

    assertThat(calc.getState(), hasItem(new BigDecimal(1)));
  }

  @Test
  public void shouldPerformMultiplication() {
    calc.push(givenNumberToken(1));
    calc.push(givenNumberToken(2));
    calc.push(givenOperatorToken(Operators.MULTIPLICATION));

    assertThat(calc.getState(), hasItem(new BigDecimal(2)));
  }

  @Test
  public void shouldPerformDivision() {
    calc.push(givenNumberToken(42));
    calc.push(givenNumberToken(4));
    calc.push(givenOperatorToken(Operators.DIVISION));

    assertThat(calc.getState(), hasItem(new BigDecimal(10.5)));
  }

  @Test
  public void shouldClearCalculator() {
    calc.push(givenNumberToken(12));
    calc.push(givenCommandToken(Commands.CLEAR));

    assertThat(calc.getState().size(), is(0));
  }

  @Test
  public void shouldUndoPush() {
    calc.push(givenNumberToken(12));
    calc.push(givenNumberToken(23));
    calc.push(givenCommandToken(Commands.UNDO));

    assertThat(calc.getState().size(), is(1));

    assertThat(calc.getState(), hasItem(new BigDecimal(12)));
  }

  @Test
  public void shouldIgnoreUndoOnEmptyStack() {
    calc.push(givenCommandToken(Commands.UNDO));

    assertThat(calc.getState().size(), is(0));
  }

  private NumberToken givenNumberToken(int value) {
    return new NumberToken(new BigDecimal(value), String.valueOf(value), -1);
  }

  private OperatorToken givenOperatorToken(final Operators op) {
    return new OperatorToken(op, op.token, -1);
  }

  private CommandToken givenCommandToken(final Commands command) {
    return new CommandToken(command, command.token, -1);
  }

  private InvalidInputToken givenInvalidInput() {
    return new InvalidInputToken("foo", -1);
  }

  private Token givenToken() {
    return new Token("foo", -1);
  }
}
