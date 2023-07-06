package org.quasar.rpn;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.quasar.rpn.tokens.CommandToken;
import org.quasar.rpn.tokens.InvalidInputToken;
import org.quasar.rpn.tokens.NumberToken;
import org.quasar.rpn.tokens.OperatorToken;
import org.quasar.rpn.tokens.Token;

public class LexerTest {
  private final Lexer lexer = new Lexer();

  @Test
  public void shouldParseNumberToken() {
    String value = "45";

    List<Token> tokens = lexer.tokenise(value);

    assertThat(tokens, hasItem(isNumberToken(new BigDecimal("45"), "45", 1)));
  }

  @Test
  public void shouldParseCommandTokens() {
    final String[] commands = new String[] { "undo", "clear" };

    Arrays.stream(commands).forEach((cmd) -> {
      List<Token> tokens = lexer.tokenise(cmd);

      assertThat(tokens, hasItem(isCommandToken(CommandToken.Commands.fromValue(cmd), cmd, 1)));
    });
  }

  @Test
  public void shouldParseOperatorTokens() {
    final String[] operators = new String[] { "sqrt", "+", "-", "*", "/" };

    Arrays.stream(operators).forEach((op) -> {
      List<Token> tokens = lexer.tokenise(op);

      assertThat(tokens, hasItem(isOperatorToken(OperatorToken.Operators.fromValue(op), op, 1)));
    });
  }

  @Test
  public void shouldCreateInvalidInputTokenForOtherInput() {
    final String input = "foo";

    List<Token> tokens = lexer.tokenise(input);

    assertThat(tokens, hasItem(withInvalidInputToken(input, 1)));
  }

  /*
   * We only need to test that we can parse a sequence of tokens, we don't have to test every combination here.
   */
  @Test
  public void shouldParseStreamOfTokens() {
    final String[] input = new String[] { "clear", "4", "+" };

    List<Token> tokens = lexer.tokenise(String.join(" ", input));

    assertThat(tokens.size(), is(3));
    assertThat(tokens.get(0), isCommandToken(CommandToken.Commands.CLEAR, input[0], 1));
    assertThat(tokens.get(1), isNumberToken(new BigDecimal("4"), input[1], 7));
    assertThat(tokens.get(2), isOperatorToken(OperatorToken.Operators.ADDITION, input[2], 9));
  }

  private Matcher<? super Token> isNumberToken(final BigDecimal number, final String tokenValue, final int tokenPos) {
    return new NumberTokenMatcher(number, tokenValue, tokenPos);
  }

  private Matcher<? super Token> isCommandToken(
      final CommandToken.Commands command,
      final String tokenValue,
      final int tokenPos
  ) {
    return new CommandTokenMatcher(command, tokenValue, tokenPos);
  }

  private Matcher<? super Token> isOperatorToken(
    final OperatorToken.Operators operator,
    final String tokenValue,
    final int tokenPos
  ) {
    return new OperatorTokenMatcher(operator, tokenValue, tokenPos);
  }

  private Matcher<? super Token> withInvalidInputToken(final String tokenValue, final int tokenPos) {
    return new TokenMatcher(InvalidInputToken.class, tokenValue, tokenPos);
  }

  private static class TokenMatcher extends TypeSafeMatcher<Token> {
    protected final Class<?> tokenType;
    protected final String tokenValue;
    protected final int tokenPos;

    public TokenMatcher(final Class<? extends Token> tokenType, final String tokenValue, final int tokenPos) {
      this.tokenType = tokenType;
      this.tokenValue = tokenValue;
      this.tokenPos = tokenPos;
    }

    @Override
    protected boolean matchesSafely(final Token token) {
      assertThat(token, is(instanceOf(tokenType)));
      assertThat(token.input, is(tokenValue));
      assertThat(token.position, is(tokenPos));

      return true;
    }

    @Override
    public void describeTo(final Description description) {
      description.appendText(description());
    }

    public String description() {
      return String.format("%s('%s', %d)", tokenType.getSimpleName(), tokenValue, tokenPos);
    }
  }

  private static class CommandTokenMatcher extends TokenMatcher {
    private final CommandToken.Commands command;

    public CommandTokenMatcher(
      final CommandToken.Commands command,
      final String tokenValue,
      final int tokenPos
    ) {
      super(CommandToken.class, tokenValue, tokenPos);

      this.command = command;
    }

    @Override
    protected boolean matchesSafely(final Token token) {
      final CommandToken commandToken = (CommandToken) token;

      assertThat(commandToken.command, is(command));

      return super.matchesSafely(token);
    }

    @Override
    public String description() {
      return String.format("%s('%s', %d, %s)", tokenType.getSimpleName(), tokenValue, tokenPos, command);
    }
  }

  private static class NumberTokenMatcher extends TokenMatcher {
    private final BigDecimal number;

    public NumberTokenMatcher(final BigDecimal number, final String tokenValue, final int tokenPos) {
      super(NumberToken.class, tokenValue, tokenPos);

      this.number = number;
    }

    @Override
    protected boolean matchesSafely(final Token token) {
      final NumberToken commandToken = (NumberToken) token;

      assertThat(commandToken.number, is(number));

      return super.matchesSafely(token);
    }

    @Override
    public String description() {
      return String.format("%s('%s', %d, %f)", tokenType.getSimpleName(), tokenValue, tokenPos, number);
    }
  }

  private static class OperatorTokenMatcher extends TokenMatcher {
    private final OperatorToken.Operators operator;

    public OperatorTokenMatcher(final OperatorToken.Operators operator, final String tokenValue, final int tokenPos) {
      super(OperatorToken.class, tokenValue, tokenPos);

      this.operator = operator;
    }

    @Override
    protected boolean matchesSafely(final Token token) {
      OperatorToken operatorToken = (OperatorToken) token;

      assertThat(operatorToken.op, is(operator));

      return super.matchesSafely(token);
    }

    @Override
    public String description() {
      return super.description();
    }
  }
}
