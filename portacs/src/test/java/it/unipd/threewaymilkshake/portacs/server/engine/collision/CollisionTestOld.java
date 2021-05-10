/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CollisionTestOld {
  @Mock Forklift first = new Forklift("1", "sfdatyrsd");
  @Mock Forklift second = new Forklift("2", "sfdyrtasd");
  @Mock Forklift third = new Forklift("3", "sfdaytrsd");

  @Mock ForkliftsList forkliftsList;

  @Mock WarehouseMap warehouseMap;

  @Test
  @DisplayName("Tests all the pipeline")
  public void pipelineTest() {
    CollisionDetector collisionDetector = new CollisionDetector();
    // CollisionSolver collisionSolver = new CollisionSolver();

    first = mock(Forklift.class);
    second = mock(Forklift.class);
    third = mock(Forklift.class);

    ReflectionTestUtils.setField(
        first, "pathToNextTask", Arrays.asList(Move.GOSTRAIGHT, Move.GOSTRAIGHT));
    ReflectionTestUtils.setField(
        second, "pathToNextTask", Arrays.asList(Move.GOSTRAIGHT, Move.GOSTRAIGHT));
    ReflectionTestUtils.setField(
        third, "pathToNextTask", Arrays.asList(Move.GOSTRAIGHT, Move.GOSTRAIGHT));
    ReflectionTestUtils.setField(first, "position", new Position(3, 3, Orientation.DOWN));
    ReflectionTestUtils.setField(second, "position", new Position(5, 1, Orientation.RIGHT));
    ReflectionTestUtils.setField(third, "position", new Position(4, 5, Orientation.LEFT));

    Map<String, Forklift> forkliftsMap = new HashMap<String, Forklift>();
    forkliftsMap.put("1", first);
    forkliftsMap.put("2", second);
    forkliftsMap.put("3", third);

    /*Map<String,List<SimplePoint>> input = new HashMap<String,List<SimplePoint>>();
    input.put("1", Arrays.asList(new SimplePoint(3,3),new SimplePoint(4,3),new SimplePoint(5,3)));
    input.put("2", Arrays.asList(new SimplePoint(5,1),new SimplePoint(5,2),new SimplePoint(5,3)));
    input.put("3", Arrays.asList(new SimplePoint(4,5),new SimplePoint(4,4),new SimplePoint(4,3)));*/

    forkliftsList = mock(ForkliftsList.class);
    ReflectionTestUtils.setField(forkliftsList, "forkliftsMap", forkliftsMap);
    warehouseMap = mock(WarehouseMap.class);
    ReflectionTestUtils.setField(collisionDetector, "warehouseMap", warehouseMap);

    when(warehouseMap.getRows()).thenReturn(10);
    when(warehouseMap.getColumns()).thenReturn(10);

    when(first.getNextPositions(2))
        .thenReturn(
            Arrays.asList(new SimplePoint(3, 3), new SimplePoint(4, 3), new SimplePoint(5, 3)));
    when(second.getNextPositions(2))
        .thenReturn(
            Arrays.asList(new SimplePoint(5, 1), new SimplePoint(5, 2), new SimplePoint(5, 3)));
    when(third.getNextPositions(2))
        .thenReturn(
            Arrays.asList(new SimplePoint(4, 5), new SimplePoint(4, 4), new SimplePoint(4, 3)));

    /*ReflectionTestUtils.setField(collisionSolver, "forkliftsList", forkliftsList);

    CollisionPipeline<ForkliftsList,Map<String, Action>> collisionPipeline
    = new CollisionPipeline<>(collisionDetector)
            .addHandler(collisionSolver);*/

    /*Map<String, Action> response = collisionPipeline.execute(forkliftsList);
    System.out.println("*******************");
    System.out.println(response);
    System.out.println("*******************");*/

    Map<SimplePoint, List<String>> response = collisionDetector.process(forkliftsList);
    System.out.printf("--------------");
    System.out.print(response);
    System.out.printf("--------------");
  }
}
