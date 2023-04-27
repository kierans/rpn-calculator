package org.quasar.rpn;

public class Calculator {
  private final Lexer lexer = new Lexer();
  private final Memory memory = new Memory();
  private final Reporter reporter = new Reporter();

  /**
   * Performs calculations and returns the current calculator {@link Memory}.
   * <p>
   * The result will contain any error messages as well.
   */
  public String[] calculate(String data) {
    try {
      lexer.tokenise(data).forEach(memory::push);

      return new String[] { reporter.format(memory.getState()) };
    }
    catch (InsufficientOperatorParametersException e) {
      return new String[] { reporter.format(e), reporter.format(memory.getState()) };
    }
    catch (IllegalArithmeticOperationException e) {
      return new String[] { reporter.format(e), reporter.format(memory.getState()) };
    }
    catch (InvalidInputException e) {
      return new String[] { reporter.format(e), reporter.format(memory.getState()) };
    }
  }
}
