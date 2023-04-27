package org.quasar.rpn;

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
      System.out.println(calculator.calculate(scanner.nextLine()));
    }
  }
}
