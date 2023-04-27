package org.quasar.rpn;

public class Calculator {
  private final Lexer lexer = new Lexer();
  private final Memory memory = new Memory();
  private final Reporter reporter = new Reporter();

  public String calculate(String data) {
    try {
      lexer.tokenise(data).forEach(memory::push);

      return reporter.format(memory.getState());
    }
    catch (InsufficientOperatorParametersException e) {
      return reporter.format(e);
    }
    catch (IllegalArithmeticOperationException e) {
      return reporter.format(e);
    }
    catch (InvalidInputException e) {
      return reporter.format(e);
    }
  }
}
