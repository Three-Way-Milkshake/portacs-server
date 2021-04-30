/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TasksSequenceTest {
  @Test
  public void testCreationAndRetrieval() {
    LinkedBlockingDeque<Long> l1 = new LinkedBlockingDeque<>();
    l1.add(1L);
    l1.add(5L);
    l1.add(7L);
    l1.add(9L);
    TasksSequence t = new TasksSequence(l1);
    assertFalse(t.isEmpty());
    assertEquals(4, t.size());
    assertEquals("LIST,1,5,7,9;", t.toString());
    assertEquals(1L, t.extractNext());
    assertEquals("LIST,5,7,9;", t.toString());
    List.of(5L, 7L, 9L).stream()
        .forEach(
            i -> {
              System.out.println(i);
              assertEquals(i, t.extractNext());
            });
    assertTrue(t.isEmpty());
  }
}
