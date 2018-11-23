package org.quasar.rpn;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

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

public class LexerTest {
  private Lexer lexer = new Lexer();

  @Test
  public void shouldParseNumberToken() {
    String value = "45";

    List<Token> tokens = lexer.tokenise(value);

    assertThat(tokens, hasItem(isNumberToken("45", 1)));
  }

  @Test
  public void shouldParseCommandTokens() {
    final String[] commands = new String[] { "undo", "clear" };

    Arrays.stream(commands).forEach((cmd) -> {
      List<Token> tokens = lexer.tokenise(cmd);

      assertThat(tokens, hasItem(isCommandToken(cmd, 1)));
    });
  }

  @Test
  public void shouldParseOperatorTokens() {
    final String[] operators = new String[] { "sqrt", "+", "-", "*", "/" };

    Arrays.stream(operators).forEach((op) -> {
      List<Token> tokens = lexer.tokenise(op);

      assertThat(tokens, hasItem(isOperatorToken(op, 1)));
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
    assertThat(tokens.get(0), isCommandToken(input[0], 1));
    assertThat(tokens.get(1), isNumberToken(input[1], 7));
    assertThat(tokens.get(2), isOperatorToken(input[2], 9));
  }

  private Matcher<? super Token> isNumberToken(final String tokenValue, final int tokenPos) {
    return new TokenMatcher(NumberToken.class, tokenValue, tokenPos);
  }

  private Matcher<? super Token> isCommandToken(final String tokenValue, final int tokenPos) {
    return new TokenMatcher(CommandToken.class, tokenValue, tokenPos);
  }

  private Matcher<? super Token> isOperatorToken(final String tokenValue, final int tokenPos) {
    return new TokenMatcher(OperatorToken.class, tokenValue, tokenPos);
  }

  private Matcher<? super Token> withInvalidInputToken(final String tokenValue, final int tokenPos) {
    return new TokenMatcher(InvalidInputToken.class, tokenValue, tokenPos);
  }

  private static class TokenMatcher extends TypeSafeMatcher<Token> {
    private final Class tokenType;
    private final String tokenValue;
    private final int tokenPos;

    public TokenMatcher(final Class tokenType, final String tokenValue, final int tokenPos) {
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
      description.appendText(String.format("%s('%s', %d)", tokenType.getSimpleName(), tokenValue, tokenPos));
    }
  }
}
