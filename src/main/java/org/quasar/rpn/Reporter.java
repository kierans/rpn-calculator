package org.quasar.rpn;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.quasar.rpn.tokens.InvalidInputToken;

public class Reporter {
  public static final DecimalFormat FORMATTER = new DecimalFormat("#.##########");

  public String format(final BigDecimal number) {
    return FORMATTER.format(number);
  }

  public String format(final InsufficientOperatorParametersException ex) {
    return String.format("operator %s (position: %d): insufficient parameters", ex.token.input, ex.token.position);
  }

  public String format(final IllegalArithmeticOperationException e) {
    return "operation '" + e.operation.asExpression() + "' is illegal";
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
