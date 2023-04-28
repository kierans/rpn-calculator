package org.quasar.rpn;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ExamplesTest {
  private Calculator calculator;

  @Before
  public void setUp() {
    calculator = new Calculator();
  }

  @Test
  public void example1() {
    final List<String> result = Arrays.asList(calculator.calculate("5 2"));

    assertThat(result, hasItem("stack: 5 2"));
  }

  @Test
  public void example2() {
    List<String> result = Arrays.asList(calculator.calculate("2 sqrt"));
    assertThat(result, hasItem("stack: 1.4142135624"));

    result = Arrays.asList(calculator.calculate("clear 9 sqrt"));
    assertThat(result, hasItem("stack: 3"));
  }

  @Test
  public void example3() {
    List<String> result = Arrays.asList(calculator.calculate("5 2 -"));
    assertThat(result, hasItem("stack: 3"));

    result = Arrays.asList(calculator.calculate("3 -"));
    assertThat(result, hasItem("stack: 0"));

    result = Arrays.asList(calculator.calculate("clear"));
    assertThat(result, hasItem("stack: "));
  }

  @Test
  public void example4() {
    List<String> result = Arrays.asList(calculator.calculate("5 4 3 2"));
    assertThat(result, hasItem("stack: 5 4 3 2"));

    result = Arrays.asList(calculator.calculate("undo undo *"));
    assertThat(result, hasItem("stack: 20"));

    result = Arrays.asList(calculator.calculate("5 *"));
    assertThat(result, hasItem("stack: 100"));

    result = Arrays.asList(calculator.calculate("undo"));
    assertThat(result, hasItem("stack: 20 5"));
  }

  @Test
  public void example5() {
    List<String> result = Arrays.asList(calculator.calculate("7 12 2 /"));
    assertThat(result, hasItem("stack: 7 6"));

    result = Arrays.asList(calculator.calculate("*"));
    assertThat(result, hasItem("stack: 42"));

    result = Arrays.asList(calculator.calculate("4 /"));
    assertThat(result, hasItem("stack: 10.5"));
  }

  @Test
  public void example6() {
    List<String> result = Arrays.asList(calculator.calculate("1 2 3 4 5"));
    assertThat(result, hasItem("stack: 1 2 3 4 5"));

    result = Arrays.asList(calculator.calculate("*"));
    assertThat(result, hasItem("stack: 1 2 3 20"));

    result = Arrays.asList(calculator.calculate("clear 3 4 -"));
    assertThat(result, hasItem("stack: -1"));
  }

  @Test
  public void example7() {
    List<String> result = Arrays.asList(calculator.calculate("1 2 3 4 5"));
    assertThat(result, hasItem("stack: 1 2 3 4 5"));

    result = Arrays.asList(calculator.calculate("* * * *"));
    assertThat(result, hasItem("stack: 120"));
  }

  @Test
  public void example8() {
    List<String> result = Arrays.asList(calculator.calculate("1 2 3 * 5 + * * 6 5"));
    assertThat(result, hasItem("operator * (position: 15): insufficient parameters"));

    // the 6 and 5 were not pushed on to the stack due to the error
    assertThat(result, hasItem("stack: 11"));
  }
}
