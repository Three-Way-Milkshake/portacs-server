package it.unipd.threewaymilkshake.portacs.server.engine;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Manager;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TasksSequenceTest {
  @Test
  public void testCreationAndRetrieval(){
    LinkedBlockingDeque<Long> l1=new LinkedBlockingDeque<>();
    l1.add(1L);
    l1.add(5L);
    l1.add(7L);
    l1.add(9L);
    TasksSequence t=new TasksSequence(l1);
    assertFalse(t.isEmpty());
    assertEquals(4, t.size());
    assertEquals("1,5,7,9", t.toString());
    l1.stream().forEach(i->{
      assertEquals(i, t.extractNext());
    });
    assertTrue(t.isEmpty());
  }
}
