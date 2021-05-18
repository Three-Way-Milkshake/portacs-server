/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import java.util.stream.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class SimplePointTest {

  @Test
  @DisplayName(
      "Tests if distance between two SimplePoint which lay on the same x-axis is computed correctly")
  public void calculateDistanceSameXAxisTest() {
    SimplePoint first = new SimplePoint(1, 1);
    SimplePoint second = new SimplePoint(1, 5);
    int returned = first.calculateDistance(second);
    assertEquals(4, returned);
  }

  @Test
  @DisplayName(
      "Tests if distance between two SimplePoint which lay on the same y-axis is computed correctly")
  public void calculateDistanceSameYAxisTest() {
    SimplePoint first = new SimplePoint(5, 1);
    SimplePoint second = new SimplePoint(1, 1);
    int returned = first.calculateDistance(second);
    assertEquals(4, returned);
  }
}
