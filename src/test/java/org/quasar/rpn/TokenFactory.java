package org.quasar.rpn;

import java.math.BigDecimal;

import org.quasar.rpn.tokens.CommandToken;
import org.quasar.rpn.tokens.InvalidInputToken;
import org.quasar.rpn.tokens.NumberToken;
import org.quasar.rpn.tokens.OperatorToken;

public class TokenFactory {
  public static NumberToken givenNumberToken(int value) {
    return new NumberToken(new BigDecimal(value), String.valueOf(value), -1);
  }

  public static OperatorToken givenOperatorToken(final OperatorToken.Operators op) {
    return new OperatorToken(op, op.token, -1);
  }

  public static CommandToken givenCommandToken(final CommandToken.Commands command) {
    return new CommandToken(command, command.token, -1);
  }

  public static InvalidInputToken givenInvalidInput() {
    return new InvalidInputToken("foo", -1);
  }

  public static Token givenToken() {
    return new Token("foo", -1);
  }
}
