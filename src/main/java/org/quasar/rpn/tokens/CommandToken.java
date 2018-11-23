package org.quasar.rpn.tokens;

import lombok.ToString;
import org.quasar.rpn.Token;

@ToString(callSuper = true)
public class CommandToken extends Token {
  public enum Commands {
    UNDO("undo"),
    CLEAR("clear");

    /** The string token that matches the command */
    public final String token;

    Commands(final String token) {
      this.token = token;
    }
  }

  public final Commands command;

  public CommandToken(final Commands command, final String input, final Integer position) {
    super(input, position);

    this.command = command;
  }
}
