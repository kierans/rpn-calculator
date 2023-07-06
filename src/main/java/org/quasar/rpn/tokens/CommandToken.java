package org.quasar.rpn.tokens;

import java.util.Arrays;

import lombok.ToString;

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

    public static Commands fromValue(String value) {
      return Arrays.stream(values())
        .filter((item) -> value.equals(item.token))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String.format("%s is not a valid command", value)));
    }
  }

  public final Commands command;

  public CommandToken(final Commands command, final String input, final Integer position) {
    super(input, position);

    this.command = command;
  }
}
