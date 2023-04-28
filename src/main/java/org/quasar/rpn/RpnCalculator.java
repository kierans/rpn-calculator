package org.quasar.rpn;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Very simple Reverse Polish Notation calculator.
 */
public class RpnCalculator {
  public static void main(String[] args) {
    final Scanner scanner = new Scanner(System.in);
    final Calculator calculator = new Calculator();

    /*
     * This sort of problem is more suited to a Event Driven/Reactive/Streaming approach.
     * This is the simplest solution, but wouldn't be suited to a high volume environment.
     */
    while (true) {
      try {
        System.out.println(String.join("\n", calculator.calculate(scanner.nextLine())));
      }
      catch (NoSuchElementException e) {
        // ignore, it's the scanner closing
        break;
      }
    }
  }
}
