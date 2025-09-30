package org.quasar.rpn;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.quasar.rpn.TokenFactory.givenInvalidInput;
import static org.quasar.rpn.TokenFactory.givenOperatorToken;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.quasar.rpn.operations.Operation;
import org.quasar.rpn.operations.OperationFactory;
import org.quasar.rpn.tokens.OperatorToken;
import org.quasar.rpn.tokens.OperatorToken.Operators;

public class ReporterTest {
  private final Reporter reporter = new Reporter();

  @Test
  public void shouldFormatNumbersToTenDecimalPlaces() {
    assertThat(reporter.format(new BigDecimal("4")), is("4"));
    assertThat(reporter.format(new BigDecimal("4.00")), is("4"));
    assertThat(reporter.format(new BigDecimal("3.141592653589793")), is("3.1415926536"));

    // check rounding around the 10th digit
    assertThat(reporter.format(new BigDecimal("3.141592653559793")), is("3.1415926536"));
    assertThat(reporter.format(new BigDecimal("3.141592653549793")), is("3.1415926535"));
  }

  @Test
  public void shouldFormatOperatorException() {
    final OperatorToken operatorToken = givenOperatorToken(Operators.MULTIPLICATION);
    final InsufficientOperatorParametersException ex = new InsufficientOperatorParametersException(operatorToken);

    final String message = reporter.format(ex);

    assertThat(message, is("operator * (position: -1): insufficient parameters"));
  }

  @Test
  public void shouldFormatIllegalArithmeticOperation() {
    final Operation operation = OperationFactory.givenNumberOperation(4);
    final IllegalArithmeticOperationException ex = new IllegalArithmeticOperationException(operation, new Exception());

    final String message = reporter.format(ex);

    assertThat(message, is("operation '" + operation.asExpression() + "' is illegal"));
  }

  @Test
  public void shouldFormatInvalidInputException() {
    final InvalidInputException ex = new InvalidInputException(givenInvalidInput());

    final String message = reporter.format(ex);

    assertThat(message, is("invalid input (position: -1): 'foo'"));
  }

  @Test
  public void shouldFormatCalculatorState() {
    final List<BigDecimal> state = Arrays.asList(new BigDecimal("42"), new BigDecimal("3.141"));

    final String message = reporter.format(state);

    assertThat(message, is("stack: 42 3.141"));
  }
}
