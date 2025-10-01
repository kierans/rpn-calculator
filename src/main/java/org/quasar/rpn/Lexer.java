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

    return splitIntoWords(input)
      .map(this::tokeniseWord)
      .collect(Collectors.toList());
  }

  private static Stream<String> splitIntoWords(final String input) {
    return Arrays.stream(input.split(" "));
  }

  private Token tokeniseWord(final String input) {
    final Token token = createTokenFromWord(input, pos);

    // add one to cater for the ' '
    pos += input.length() + 1;

    return token;
  }

  private Token createTokenFromWord(final String input, final int pos) {
    return determineTokenType(input).apply(pos);
  }

  private Function<Integer, Token> determineTokenType(final String input) {
    /*
     * Pipe the raw token through a stream of functions, until we find the first token type that matches the input.
     */
    final Stream<Function<String, Optional<Function<Integer, Token>>>> tokenIdentifiers =
        Stream.of(this::isCommandToken, this::isOperatorToken, this::isNumberToken);

    return tokenIdentifiers
        .map((fn) -> fn.apply(input))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .orElseGet(() -> (pos) -> new InvalidInputToken(input, pos));
  }

  private Optional<Function<Integer, Token>> isCommandToken(final String input) {
    return Arrays.stream(CommandToken.Commands.values())
      .filter((cmd) -> cmd.token.equals(input))
      .findFirst()
      .map((cmd) -> (pos) -> new CommandToken(cmd, input, pos));
  }

  private Optional<Function<Integer, Token>> isOperatorToken(final String input) {
    return Arrays.stream(OperatorToken.Operators.values())
        .filter((op) -> op.token.equals(input))
        .findFirst()
        .map((op) -> (pos) -> new OperatorToken(op, input, pos));
  }

  private Optional<Function<Integer, Token>> isNumberToken(final String input) {
    try {
      final BigDecimal number = new BigDecimal(input);

      return Optional.of((pos) -> new NumberToken(number, input, pos));
    }
    catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  private void reset() {
    this.pos = 1;
  }
}
