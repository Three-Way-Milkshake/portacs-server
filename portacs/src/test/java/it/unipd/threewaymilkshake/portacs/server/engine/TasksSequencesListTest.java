/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TasksSequencesListTest {
  @Test
  public void testSequencesCreationAndRetrieval() {
    TasksSequencesList tsl = new TasksSequencesList();
    LinkedBlockingDeque<Long> l1 = new LinkedBlockingDeque<>();
    l1.add(1L);
    l1.add(5L);
    l1.add(7L);
    LinkedBlockingDeque<Long> l2 = new LinkedBlockingDeque<>();
    l2.add(3L);
    l2.add(6L);
    l2.add(9L);
    long id1 = tsl.addTasksSequence(l1);
    long id2 = tsl.addTasksSequence(l2);
    assertEquals(6, id1);
    assertEquals(7, id2);

    assertFalse(tsl.removeTasksSequence(10));
    assertTrue(tsl.removeTasksSequence(7));

    for (int i = 1; i <= 5; ++i) tsl.removeTasksSequence(i);
    TasksSequence ts1 = tsl.getTasksSequence();
    l1.stream()
        .forEach(
            e -> {
              assertEquals(e, ts1.extractNext());
            });
  }
}
