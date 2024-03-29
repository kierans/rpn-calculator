package org.quasar.rpn;

import java.math.BigDecimal;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.quasar.rpn.operations.ArithmeticOperation;
import org.quasar.rpn.operations.BiOperandArithmeticOperation;
import org.quasar.rpn.operations.Operation;
import org.quasar.rpn.operations.PushNumberOperation;
import org.quasar.rpn.operations.UniOperandArithmeticOperation;
import org.quasar.rpn.tokens.CommandToken;
import org.quasar.rpn.tokens.InvalidInputToken;
import org.quasar.rpn.tokens.NumberToken;
import org.quasar.rpn.tokens.OperatorToken;
import org.quasar.rpn.tokens.Token;

public class Memory {
  private final Stack<Operation> operations;

  public Memory() {
    this.operations = new Stack<>();
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
    this.operations.add(new PushNumberOperation(token));
  }

  public void push(final InvalidInputToken token) {
    throw new InvalidInputException(token);
  }

  public void push(final CommandToken token) {
    switch (token.command) {
      case UNDO:
        if (operations.size() > 0) {
          undoOperation(operations.pop());
        }

        return;

      case CLEAR:
        operations.clear();
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
    return operations.stream()
      .map(Operation::getValue)
      .collect(Collectors.toList());
  }

  private void doUniOperandOperation(final OperatorToken token) {
    if (operations.size() < 1) {
      throw new InsufficientOperatorParametersException(token);
    }

    computeOperationValue(new UniOperandArithmeticOperation(token, operations.pop()));
  }

  private void doBiOperandOperation(final OperatorToken token) {
    if (operations.size() < 2) {
      throw new InsufficientOperatorParametersException(token);
    }

    final Operation b = operations.pop();
    final Operation a = operations.pop();

    computeOperationValue(new BiOperandArithmeticOperation(token, a, b));
  }

  private void computeOperationValue(final ArithmeticOperation op) {
    try {
      op.computeValue();
      operations.push(op);
    }
    catch (IllegalArithmeticOperationException e) {
      undoOperation(op);

      throw e;
    }
  }

  private void undoOperation(final Operation op) {
    operations.addAll(op.undo());
  }
}
