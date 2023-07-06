package org.quasar.rpn.tokens;

import java.util.Arrays;

import lombok.ToString;

@ToString(callSuper = true)
public class OperatorToken extends Token {
  public enum Operators {
    SQUARE_ROOT("sqrt"),
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/");

    /** The string token that matches the command */
    public final String token;

    Operators(final String token) {
      this.token = token;
    }

    public static OperatorToken.Operators fromValue(String value) {
      return Arrays.stream(values())
        .filter((item) -> value.equals(item.token))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String.format("%s is not a valid operator", value)));
    }
  }

  public final Operators op;

  public OperatorToken(final Operators op, final String input, final Integer position) {
    super(input, position);

    this.op = op;
  }
}
