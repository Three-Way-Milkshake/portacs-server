/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import java.util.HashMap;
import java.util.LinkedList;
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
public class CollisionDetectionTest {

  @Mock Forklift forklift;

  @Mock CollisionMap collisionMap;

  @Mock WarehouseMap warehouseMap;

  CollisionDetection collisionDetection;

  @Test
  @DisplayName("Test for Collision detection")
  public void processTest() {
    forklift = mock(Forklift.class);
    collisionMap = mock(CollisionMap.class);
    warehouseMap = mock(WarehouseMap.class);

    when(warehouseMap.getRows()).thenReturn(10);
    when(warehouseMap.getColumns()).thenReturn(10);

    CollisionForklift first = new CollisionForklift(forklift);
    CollisionForklift second = new CollisionForklift(forklift);
    CollisionForklift third = new CollisionForklift(forklift);
    List<CollisionForklift> collisionForklift =
        new LinkedList<CollisionForklift>(List.of(first, second, third));
    List<SimplePoint> firstForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(1, 1), new SimplePoint(1, 2), new SimplePoint(1, 3)));
    List<SimplePoint> secondForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(3, 2), new SimplePoint(2, 2), new SimplePoint(1, 2)));
    List<SimplePoint> thirdForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(2, 4), new SimplePoint(2, 3), new SimplePoint(2, 2)));

    when(forklift.getNextPositions(2))
        .thenReturn(firstForkliftPoints, secondForkliftPoints, thirdForkliftPoints);

    Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
    collisions.put(
        new SimplePoint(1, 2), new LinkedList<CollisionForklift>(List.of(first, second)));
    collisions.put(
        new SimplePoint(232, 2), new LinkedList<CollisionForklift>(List.of(second, third)));

    when(collisionMap.getCollisions()).thenReturn(collisions);

    Map<SimplePoint, List<CollisionForklift>> collisionsToCompareWith = new HashMap<>();
    collisionsToCompareWith.put(
        new SimplePoint(1, 2), new LinkedList<CollisionForklift>(List.of(second, first)));
    collisionsToCompareWith.put(
        new SimplePoint(2, 2), new LinkedList<CollisionForklift>(List.of(second, third)));

    collisionDetection = new CollisionDetection();
    collisionDetection.setWarehouseMap(warehouseMap);
    ReflectionTestUtils.setField(collisionDetection, "collisionSum", collisionMap);

    Map<SimplePoint, List<CollisionForklift>> collisionsReturned =
        collisionDetection.process(collisionForklift);

    assertEquals(collisionsToCompareWith, collisionsReturned);
  }
}
