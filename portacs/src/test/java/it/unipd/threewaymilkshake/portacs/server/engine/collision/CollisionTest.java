/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
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
public class CollisionTest {

  @Mock ForkliftsList forkliftsList;

  @Mock WarehouseMap warehouseMap;

  @Test
  @DisplayName("Tests all the pipeline")
  public void pipelineTest() {
    CollisionDetector collisionDetector = new CollisionDetector();
    CollisionSolver collisionSolver = new CollisionSolver();

    Map<String, List<SimplePoint>> input = new HashMap<String, List<SimplePoint>>();
    input.put(
        "1", Arrays.asList(new SimplePoint(3, 3), new SimplePoint(4, 3), new SimplePoint(5, 3)));
    input.put(
        "2", Arrays.asList(new SimplePoint(4, 1), new SimplePoint(4, 2), new SimplePoint(4, 3)));
    input.put(
        "3", Arrays.asList(new SimplePoint(4, 5), new SimplePoint(4, 4), new SimplePoint(4, 3)));

    forkliftsList = mock(ForkliftsList.class);
    warehouseMap = mock(WarehouseMap.class);
    ReflectionTestUtils.setField(collisionDetector, "warehouseMap", warehouseMap);
    when(forkliftsList.getAllNextPositions(2)).thenReturn(input);

    when(forkliftsList.getSimplePointFromString("1")).thenReturn(new SimplePoint(3, 3));
    when(forkliftsList.getSimplePointFromString("2")).thenReturn(new SimplePoint(4, 1));
    when(forkliftsList.getSimplePointFromString("3")).thenReturn(new SimplePoint(4, 5));

    when(forkliftsList.headOnRisk("1", "2")).thenReturn(false);
    when(forkliftsList.headOnRisk("2", "1")).thenReturn(false);
    when(forkliftsList.headOnRisk("1", "3")).thenReturn(false);
    when(forkliftsList.headOnRisk("3", "1")).thenReturn(false);
    when(forkliftsList.headOnRisk("3", "2")).thenReturn(true);
    when(forkliftsList.headOnRisk("2", "3")).thenReturn(true);

    ReflectionTestUtils.setField(collisionSolver, "forkliftsList", forkliftsList);

    when(warehouseMap.getRows()).thenReturn(10);
    when(warehouseMap.getColumns()).thenReturn(10);

    CollisionPipeline<ForkliftsList, Map<String, Action>> collisionPipeline =
        new CollisionPipeline<>(collisionDetector).addHandler(collisionSolver);

    Map<String, Action> response = collisionPipeline.execute(forkliftsList);
    System.out.print("--------------");
    System.out.print(response);
    System.out.print("--------------");

    /*Map<SimplePoint,List<String>> response = collisionDetector.process(forkliftsList);
    System.out.printf("--------------");
    System.out.print(response);
    System.out.printf("--------------");*/

  }

  @Test
  @DisplayName("Tests all the pipeline")
  public void pipeline2Test() {
    CollisionDetector collisionDetector = new CollisionDetector();
    CollisionSolver collisionSolver = new CollisionSolver();

    Map<String, List<SimplePoint>> input = new HashMap<String, List<SimplePoint>>();
    input.put(
        "1", Arrays.asList(new SimplePoint(0, 0), new SimplePoint(0, 0), new SimplePoint(1, 0)));
    input.put(
        "2", Arrays.asList(new SimplePoint(2, 0), new SimplePoint(2, 0), new SimplePoint(1, 0)));

    forkliftsList = mock(ForkliftsList.class);
    warehouseMap = mock(WarehouseMap.class);
    ReflectionTestUtils.setField(collisionDetector, "warehouseMap", warehouseMap);
    when(forkliftsList.getAllNextPositions(2)).thenReturn(input);

    when(forkliftsList.getSimplePointFromString("1")).thenReturn(new SimplePoint(0, 0));
    when(forkliftsList.getSimplePointFromString("2")).thenReturn(new SimplePoint(2, 0));

    when(forkliftsList.headOnRisk("1", "2")).thenReturn(true);
    when(forkliftsList.headOnRisk("2", "1")).thenReturn(true);

    ReflectionTestUtils.setField(collisionSolver, "forkliftsList", forkliftsList);

    when(warehouseMap.getRows()).thenReturn(4);
    when(warehouseMap.getColumns()).thenReturn(4);

    CollisionPipeline<ForkliftsList, Map<String, Action>> collisionPipeline =
        new CollisionPipeline<>(collisionDetector).addHandler(collisionSolver);

    Map<String, Action> response = collisionPipeline.execute(forkliftsList);
    System.out.print("--------------");
    System.out.print(response);
    System.out.print("--------------");

    /*Map<SimplePoint,List<String>> response = collisionDetector.process(forkliftsList);
    System.out.printf("--------------");
    System.out.print(response);
    System.out.printf("--------------");*/

  }
}
