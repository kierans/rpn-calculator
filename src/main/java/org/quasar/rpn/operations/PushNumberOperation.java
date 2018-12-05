package org.quasar.rpn.operations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.quasar.rpn.tokens.NumberToken;

public class PushNumberOperation implements Operation {
  public final NumberToken token;

  public PushNumberOperation(final NumberToken token) {
    this.token = token;
  }

  @Override
  public BigDecimal getValue() {
    return token.number;
  }

  @Override
  public List<Operation> undo() {
    return Collections.emptyList();
  }

  @Override
  public String asExpression() {
    return token.input;
  }
}
