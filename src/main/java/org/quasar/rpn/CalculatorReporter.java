package org.quasar.rpn;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.quasar.rpn.tokens.InvalidInputToken;
import org.quasar.rpn.tokens.OperatorToken;

public class CalculatorReporter {
  public static final DecimalFormat FORMATTER = new DecimalFormat("#.##########");

  public String format(final BigDecimal number) {
    return FORMATTER.format(number);
  }

  public String format(final InsufficientOperatorParametersException ex) {
    final OperatorToken operatorToken = ex.operatorToken;

    return String.format("operator %s (position: %d): insufficient parameters", operatorToken.input, operatorToken.position);
  }

  public String format(final InvalidInputException ex) {
    final InvalidInputToken token = ex.token;

    return String.format("invalid input (position: %d): '%s'", token.position, token.input);
  }

  public String format(final List<BigDecimal> state) {
    final String stack = state.stream().map(FORMATTER::format).collect(Collectors.joining(" "));

    return String.format("stack: %s", stack);
  }
}
