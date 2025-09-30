package org.quasar.rpn;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CalculatorTest {
  @Test
  public void shouldReturnStackContents() {
    final List<String> result = Arrays.asList(new Calculator().calculate("1 2 +"));

    assertThat(result.size(), is(1));
    assertThat(result, hasItem(equalTo("stack: 3")));
  }

  @Test
  public void shouldReturnErrorAndStackContents() {
    final String data = "1 0 /";
    final List<String> result = Arrays.asList(new Calculator().calculate(data));

    assertThat(result.size(), is(2));
    assertThat(result, hasItem(equalTo(String.format("operation '%s' is illegal", data))));
    assertThat(result, hasItem(equalTo("stack: 1 0")));
  }
}
