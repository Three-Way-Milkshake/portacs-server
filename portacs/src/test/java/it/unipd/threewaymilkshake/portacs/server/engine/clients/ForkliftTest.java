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
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ForkliftTest {

  Forklift forklift;

  @Autowired private TasksSequencesList tasksSequencesListTest;

  @Captor private ArgumentCaptor<String> outCaptor;

  @Test
  @DisplayName("Tests if the next positions are returned correctly: unit is active")
  public void getNextPositionsWithActiveUnitTest() {
    forklift = new Forklift("forklift", "abcdefghi1234");
    ReflectionTestUtils.setField(forklift, "active", true);
    forklift.setPosition(new Position(3, 4, Orientation.RIGHT));
    List<Move> pathToNextTask = Arrays.asList(Move.GOSTRAIGHT, Move.TURNLEFT, Move.GOSTRAIGHT);
    forklift.setPathToNextTask(pathToNextTask);

    List<SimplePoint> returned = forklift.getNextPositions(2);
    List<SimplePoint> toCompare =
        Arrays.asList(new SimplePoint(3, 4), new SimplePoint(3, 5), new SimplePoint(3, 5));

    assertEquals(toCompare, returned);
  }

  @Test
  @DisplayName("Tests if the next positions are returned correctly: unit is not active")
  public void getNextPositionsWithNotActiveUnitTest() {
    forklift = new Forklift("forklift", "abcdefghi1234");
    ReflectionTestUtils.setField(forklift, "active", false);
    forklift.setPosition(new Position(3, 4, Orientation.RIGHT));
    List<Move> pathToNextTask = Arrays.asList(Move.GOSTRAIGHT, Move.TURNLEFT, Move.GOSTRAIGHT);
    forklift.setPathToNextTask(pathToNextTask);

    List<SimplePoint> returned = forklift.getNextPositions(2);
    List<SimplePoint> toCompare =
        Arrays.asList(new SimplePoint(3, 4), new SimplePoint(3, 4), new SimplePoint(3, 4));

    assertEquals(toCompare, returned);
  }

  @Test
  @Disabled
  public void testTasksToString() throws IOException {
    BufferedReader in = mock(BufferedReader.class);
    PrintWriter out = mock(PrintWriter.class);
    Connection c = new Connection(null, in, out);
    when(in.readLine()).thenReturn("LIST");
    Forklift f = new Forklift("cesare", "abc", tasksSequencesListTest);
    f.setWarehouseMap(mock(WarehouseMap.class));
    f.bindConnection(c);
    f.processCommunication();

    verify(out, atLeastOnce()).println("ALIVE;");
    // verify(out, atLeastOnce()).print("LIST,1,2,3;");

    // assertEquals("3,1,2,3", f.getTasksString());
  }
}
