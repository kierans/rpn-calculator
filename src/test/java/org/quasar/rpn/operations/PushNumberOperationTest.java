package org.quasar.rpn.operations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.quasar.rpn.TokenFactory.givenNumberToken;

import java.math.BigDecimal;

import org.junit.Test;

public class PushNumberOperationTest {
  @Test
  public void shouldReturnTokenValue() {
    final int value = 36;

    final PushNumberOperation op = new PushNumberOperation(givenNumberToken(value));

    assertThat(op.getValue(), is(new BigDecimal(value)));
  }

  /*
   * Undoing this operation just means removing the number from the calculator.
   */
  @Test
  public void shouldReturnEmptyListToUndoOperation() {
    final PushNumberOperation op = new PushNumberOperation(givenNumberToken(42));

    assertThat(op.undo().isEmpty(), is(true));
  }
}
