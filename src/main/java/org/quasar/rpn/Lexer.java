package org.quasar.rpn;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.quasar.rpn.tokens.CommandToken;
import org.quasar.rpn.tokens.InvalidInputToken;
import org.quasar.rpn.tokens.NumberToken;
import org.quasar.rpn.tokens.OperatorToken;
import org.quasar.rpn.tokens.Token;

public class Lexer {
  /*
   * Instead of using an attribute we could use a closure, but this is Java so icky.
   */
  private int pos;

  public List<Token> tokenise(final String input) {
    reset();

    return Arrays.stream(input.split(" "))
      .map(this::lexToken)
      .map(this::determineTokenType)
      .collect(Collectors.toList());
  }

  private Token lexToken(final String input) {
    final Token token = new Token(input, pos);

    // add one to cater for the ' '
    pos += input.length() + 1;

    return token;
  }

  private Token determineTokenType(final Token rawToken) {
    /*
     * Pipe the raw token through a stream of functions, until we find the first token type that matches the input.
     */
    final Stream<Function<Token, Optional<Token>>> tokenIdentifiers =
        Stream.of(this::isCommandToken, this::isOperatorToken, this::isNumberToken);

    return tokenIdentifiers
        .map((fn) -> fn.apply(rawToken))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .orElseGet(() -> this.invalidInput(rawToken));
  }

  private Optional<Token> isCommandToken(final Token token) {
    return Arrays.stream(CommandToken.Commands.values())
      .filter((cmd) -> cmd.token.equals(token.input))
      .findFirst()
      .map((cmd) -> new CommandToken(cmd, token.input, token.position));
  }

  private Optional<Token> isOperatorToken(final Token token) {
    return Arrays.stream(OperatorToken.Operators.values())
        .filter((op) -> op.token.equals(token.input))
        .findFirst()
        .map((op) -> new OperatorToken(op, token.input, token.position));
  }

  private Optional<Token> isNumberToken(final Token rawToken) {
    try {
      final String input = rawToken.input;
      final BigDecimal number = new BigDecimal(input);

      return Optional.of(new NumberToken(number, input, rawToken.position));
    }
    catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  private Token invalidInput(final Token rawToken) {
    return new InvalidInputToken(rawToken.input, rawToken.position);
  }

  private void reset() {
    this.pos = 1;
  }
}
