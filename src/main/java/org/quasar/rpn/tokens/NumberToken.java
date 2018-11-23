package org.quasar.rpn.tokens;

import java.math.BigDecimal;

import lombok.ToString;
import org.quasar.rpn.Token;

@ToString(callSuper = true)
public class NumberToken extends Token {
  public final BigDecimal number;

  public NumberToken(final BigDecimal number, final String input, final Integer position) {
    super(input, position);

    this.number = number;
  }
}
