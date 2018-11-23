package org.quasar.rpn;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.quasar.rpn.tokens.InvalidInputToken;
import org.quasar.rpn.tokens.OperatorToken;
import org.quasar.rpn.tokens.OperatorToken.Operators;

public class CalculatorReporterTest {
  private CalculatorReporter reporter = new CalculatorReporter();

  @Test
  public void shouldFormatNumbersToTenDecimalPlaces() {
    assertThat(reporter.format(new BigDecimal(3.141592653589793)), is("3.1415926536"));

    // check rounding around the 10th digit
    assertThat(reporter.format(new BigDecimal(3.141592653559793)), is("3.1415926536"));
    assertThat(reporter.format(new BigDecimal(3.141592653549793)), is("3.1415926535"));
  }

  @Test
  public void shouldFormatOperatorException() {
    final OperatorToken operatorToken = new OperatorToken(Operators.MULTIPLICATION, "*", 45);
    final InsufficientOperatorParametersException ex = new InsufficientOperatorParametersException(operatorToken);

    final String message = reporter.format(ex);

    assertThat(message, is("operator * (position: 45): insufficient parameters"));
  }

  @Test
  public void shouldFormatInvalidInputException() {
    final InvalidInputToken token = new InvalidInputToken("foo", 23);
    final InvalidInputException ex = new InvalidInputException(token);

    final String message = reporter.format(ex);

    assertThat(message, is("invalid input (position: 23): 'foo'"));
  }

  @Test
  public void shouldFormatCalculatorState() {
    final List<BigDecimal> state = Arrays.asList(new BigDecimal("42"), new BigDecimal("3.141"));

    final String message = reporter.format(state);

    assertThat(message, is("stack: 42 3.141"));
  }
}
