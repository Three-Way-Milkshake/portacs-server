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
import java.util.Set;
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
public class CollisionMapTest {

  @Mock WarehouseMap warehouseMap;

  @Mock Forklift forklift;

  @Mock CollisionMap collisionMap;

  @Test
  @DisplayName("Test of sum")
  public void sumTest() {
    collisionMap = mock(CollisionMap.class);
    warehouseMap = mock(WarehouseMap.class);
    when(warehouseMap.getRows()).thenReturn(10);
    when(warehouseMap.getColumns()).thenReturn(10);
    when(warehouseMap.isBasePOI(new SimplePoint(1, 1))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(1, 2))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(1, 3))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(2, 2))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(2, 3))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(2, 4))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(3, 2))).thenReturn(false);

    when(collisionMap.checkMapDomain(new SimplePoint(1, 1))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(1, 2))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(1, 3))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(2, 2))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(2, 3))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(2, 4))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(3, 2))).thenReturn(true);

    collisionMap = new CollisionMap(warehouseMap);
    CollisionCell[][] map = new CollisionCell[10][10];
    ReflectionTestUtils.setField(collisionMap, "map", map);

    forklift = mock(Forklift.class);

    CollisionForklift first = new CollisionForklift(forklift);
    CollisionForklift second = new CollisionForklift(forklift);
    CollisionForklift third = new CollisionForklift(forklift);
    List<SimplePoint> firstForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(1, 1), new SimplePoint(1, 2), new SimplePoint(1, 3)));
    List<SimplePoint> secondForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(3, 2), new SimplePoint(2, 2), new SimplePoint(1, 2)));
    List<SimplePoint> thirdForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(2, 4), new SimplePoint(2, 3), new SimplePoint(2, 2)));
    Map<CollisionForklift, List<SimplePoint>> nextMoves = new HashMap<>();
    nextMoves.put(first, firstForkliftPoints);
    nextMoves.put(second, secondForkliftPoints);
    nextMoves.put(third, thirdForkliftPoints);

    collisionMap.sum(nextMoves);

    CollisionCell[][] returned = collisionMap.getMap();
    CollisionCell toCompare = returned[1][2];
    CollisionCell expected = new CollisionCell();
    expected.addForklift(second);
    expected.addForklift(first);

    Set<CollisionForklift> expectedSet =
        (Set<CollisionForklift>) ReflectionTestUtils.getField(expected, "collisionsForCell");
    Set<CollisionForklift> toCompareSet =
        (Set<CollisionForklift>) ReflectionTestUtils.getField(toCompare, "collisionsForCell");
    assertEquals(expectedSet, toCompareSet);
  }

  @Test
  public void getCollisionsTest() {
    collisionMap = mock(CollisionMap.class);
    warehouseMap = mock(WarehouseMap.class);
    when(warehouseMap.getRows()).thenReturn(10);
    when(warehouseMap.getColumns()).thenReturn(10);
    when(warehouseMap.isBasePOI(new SimplePoint(1, 1))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(1, 2))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(1, 3))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(2, 2))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(2, 3))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(2, 4))).thenReturn(false);
    when(warehouseMap.isBasePOI(new SimplePoint(3, 2))).thenReturn(false);

    when(collisionMap.checkMapDomain(new SimplePoint(1, 1))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(1, 2))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(1, 3))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(2, 2))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(2, 3))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(2, 4))).thenReturn(true);
    when(collisionMap.checkMapDomain(new SimplePoint(3, 2))).thenReturn(true);

    collisionMap = new CollisionMap(warehouseMap);
    CollisionCell[][] map = new CollisionCell[10][10];
    ReflectionTestUtils.setField(collisionMap, "map", map);

    forklift = mock(Forklift.class);

    CollisionForklift first = new CollisionForklift(forklift);
    CollisionForklift second = new CollisionForklift(forklift);
    CollisionForklift third = new CollisionForklift(forklift);
    List<SimplePoint> firstForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(1, 1), new SimplePoint(1, 2), new SimplePoint(1, 3)));
    List<SimplePoint> secondForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(3, 2), new SimplePoint(2, 2), new SimplePoint(1, 2)));
    List<SimplePoint> thirdForkliftPoints =
        new LinkedList<SimplePoint>(
            List.of(new SimplePoint(2, 4), new SimplePoint(2, 3), new SimplePoint(2, 2)));
    Map<CollisionForklift, List<SimplePoint>> nextMoves = new HashMap<>();
    nextMoves.put(first, firstForkliftPoints);
    nextMoves.put(second, secondForkliftPoints);
    nextMoves.put(third, thirdForkliftPoints);

    collisionMap.sum(nextMoves);

    Map<SimplePoint, List<CollisionForklift>> returned = collisionMap.getCollisions();
    Map<SimplePoint, List<CollisionForklift>> expected = new HashMap<>();
    expected.put(new SimplePoint(1, 2), new LinkedList<CollisionForklift>(List.of(second, first)));
    expected.put(new SimplePoint(2, 2), new LinkedList<CollisionForklift>(List.of(second, third)));
    assertEquals(expected, expected);
  }
}
