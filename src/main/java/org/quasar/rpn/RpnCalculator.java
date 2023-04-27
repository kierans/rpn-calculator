package org.quasar.rpn;

import java.util.Scanner;

/**
 * Very simple Reverse Polish Notation calculator.
 */
public class RpnCalculator {
  public static void main(String[] args) {
    final Scanner scanner = new Scanner(System.in);
    final Lexer lexer = new Lexer();
    final Memory memory = new Memory();
    final Reporter reporter = new Reporter();

    /*
     * This sort of problem is more suited to a Event Driven/Reactive/Streaming approach.
     * This is the simplest solution, but wouldn't be suited to a high volume environment.
     */
    while (true) {
      try {
        lexer.tokenise(scanner.nextLine()).forEach(memory::push);
      }
      catch (InsufficientOperatorParametersException e) {
        System.out.println(reporter.format(e));
      }
      catch (IllegalArithmeticOperationException e) {
        System.out.println(reporter.format(e));
      }
      catch (InvalidInputException e) {
        System.out.println(reporter.format(e));
      }
      finally {
        System.out.println(reporter.format(memory.getState()));
      }
    }
  }
}
