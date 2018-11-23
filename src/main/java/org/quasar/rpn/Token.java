package org.quasar.rpn;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Token {
  /** The parsed input of the token */
  public final String input;

  /** The position in the input the token is */
  public final Integer position;
}
