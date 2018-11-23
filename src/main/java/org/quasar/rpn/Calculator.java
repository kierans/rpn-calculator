package org.quasar.rpn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.quasar.rpn.tokens.CommandToken;
import org.quasar.rpn.tokens.InvalidInputToken;
import org.quasar.rpn.tokens.NumberToken;
import org.quasar.rpn.tokens.OperatorToken;

public class Calculator {
  private Stack<BigDecimal> stack;

  public Calculator() {
    this.stack = new Stack<>();
  }

  public void push(final Token token) {
    // Java's type system sucks.
    switch (token.getClass().getSimpleName()) {
      case "CommandToken":
        this.push((CommandToken) token);
        return;

      case "NumberToken":
        this.push((NumberToken) token);
        return;

      case "InvalidInputToken":
        this.push((InvalidInputToken) token);
        return;

      case "OperatorToken":
        this.push((OperatorToken) token);
        return;
    }

    throw new IllegalStateException(String.format("Can't handle token of type %s", token.getClass().getSimpleName()));
  }

  public void push(final NumberToken token) {
    this.stack.add(token.number);
  }

  public void push(final InvalidInputToken token) {
    throw new InvalidInputException(token);
  }

  public void push(final CommandToken token) {
    switch (token.command) {
      case UNDO:
        if (stack.size() > 0) {
          stack.pop();
        }

        return;

      case CLEAR:
        stack.clear();
    }
  }

  public void push(final OperatorToken token) {
    switch (token.op) {
      case SQUARE_ROOT:
        doUniOperandOperation(token);

        return;

      // all other operators require two operands
      default:
        doBiOperandOperation(token);
    }
  }

  /**
   * @return A copy of the calculator's state.
   */
  public List<BigDecimal> getState() {
    return new ArrayList<>(stack);
  }

  private void doUniOperandOperation(final OperatorToken token) {
    if (stack.size() < 1) {
      throw new InsufficientOperatorParametersException(token);
    }

    final BigDecimal a = stack.pop();

    /*
     * Currently we only have one uni operator.
     *
     * Math.sqrt should be sufficient without losing precision
     */
    stack.push(new BigDecimal(Math.sqrt(a.doubleValue())));
  }

  private void doBiOperandOperation(final OperatorToken token) {
    if (stack.size() < 2) {
      throw new InsufficientOperatorParametersException(token);
    }

    final BigDecimal b = stack.pop();
    final BigDecimal a = stack.pop();

    switch (token.op) {
      case ADDITION:
        stack.push(a.add(b));

        return;

      case SUBTRACTION:
        stack.push(a.subtract(b));

        return;

      case MULTIPLICATION:
        stack.push(a.multiply(b));

        return;

      case DIVISION:
        stack.push(new BigDecimal(a.doubleValue() / b.doubleValue()));
    }
  }
}
