/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequencesList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ForkliftTest {

  Forklift forklift;

  @Autowired private TasksSequencesList tasksSequencesListTest;

  @Captor private ArgumentCaptor<String> outCaptor;

  @BeforeEach
  public void setUp() {
    forklift = new Forklift("forklift", "abcdefghi1234");
    forklift.setPosition(new Position(3, 4, Orientation.RIGHT));
    List<Move> pathToNextTask = Arrays.asList(Move.GOSTRAIGHT, Move.TURNLEFT, Move.GOSTRAIGHT);
    forklift.setPathToNextTask(pathToNextTask);
  }

  @Test
  @DisplayName("Tests if getNextPositions work as expected")
  public void getNextPositionsTest() {
    List<SimplePoint> returned = forklift.getNextPositions(2);
    List<SimplePoint> toCompare =
        Arrays.asList(
            new SimplePoint(3, 4),
            new SimplePoint(3, 5),
            new SimplePoint(3, 5),
            new SimplePoint(3, 5));
    // System.out.println("*****************************************************");
    IntStream.range(0, returned.size())
        .forEach(
            i -> {
              assertEquals(returned.get(i), toCompare.get(i));
            });
    // System.out.println("*****************************************************");
  }

  @Test
  public void testTasksToString() throws IOException {
    BufferedReader in = mock(BufferedReader.class);
    PrintWriter out = mock(PrintWriter.class);
    Connection c = new Connection(null, in, out);
    when(in.readLine()).thenReturn("LIST");
    Forklift f = new Forklift("cesare", "abc", tasksSequencesListTest);
    f.bindConnection(c);
    f.processCommunication();

    verify(out, atLeastOnce()).println("ALIVE;");
    verify(out, atLeastOnce()).print("LIST,1,2,3;");

    assertEquals("3,1,2,3", f.getTasksString());
  }
}
