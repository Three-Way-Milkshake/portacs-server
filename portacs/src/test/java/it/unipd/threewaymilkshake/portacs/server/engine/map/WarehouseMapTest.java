/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Manager;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class WarehouseMapTest {

  @Test
  public void testPropertyChangeMechanism() {
    CellType[][] arr =
        new CellType[][] {
          {CellType.OBSTACLE, CellType.OBSTACLE},
          {CellType.NEUTRAL, CellType.NEUTRAL}
        };
    CellType[][] arr2 =
        new CellType[][] {
          {CellType.NEUTRAL, CellType.NEUTRAL},
          {CellType.NEUTRAL, CellType.NEUTRAL}
        };
    List<Poi> pois = new ArrayList<>();
    pois.add(new Poi(1L, "test", new SimplePoint(0, 0), PoiType.LOAD));
    WarehouseMap map = new WarehouseMap(arr, pois, null);
    assertEquals(arr, map.getMap());
    // User observer=new Manager("id", "firstName", "lastName", "pwdHash");
    User observer = mock(Manager.class);
    // WarehouseMap map2=new WarehouseMap(mock(MapDao.class), null);
    verifyNoInteractions(observer);
    // observer.setWarehouseMap(map);
    map.addPropertyChangeListener(observer);
    // map.setMap(arr2);
    // verify(observer, times(1)).propertyChange(any());
    // verifyNoMoreInteractions(observer);
  }

  @Test
  public void testMapCreationAndIntConversion() {
    CellType[][] arr = {
      {CellType.RIGHT, CellType.RIGHT, CellType.RIGHT, CellType.NEUTRAL},
      {CellType.NEUTRAL, CellType.OBSTACLE, CellType.OBSTACLE, CellType.NEUTRAL},
      {CellType.NEUTRAL, CellType.OBSTACLE, CellType.OBSTACLE, CellType.NEUTRAL},
      {CellType.LEFT, CellType.LEFT, CellType.LEFT, CellType.LEFT}
    };
    List<Poi> pois = new ArrayList<>();
    pois.add(new Poi(1L, "test", new SimplePoint(0, 0), PoiType.LOAD));
    WarehouseMap map = new WarehouseMap(arr, pois, new StrategyBreadthFirst());

    int[][] expected = {
      {3, 3, 3, 1},
      {1, 0, 0, 1},
      {1, 0, 0, 1},
      {5, 5, 5, 5}
    };
    int[][] actual = map.getIntMatrix();
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testMapAndPoiToString() {
    CellType[][] arr = {
      {CellType.POI, CellType.RIGHT, CellType.RIGHT, CellType.NEUTRAL},
      {CellType.NEUTRAL, CellType.OBSTACLE, CellType.OBSTACLE, CellType.NEUTRAL},
      {CellType.NEUTRAL, CellType.POI, CellType.OBSTACLE, CellType.NEUTRAL},
      {CellType.LEFT, CellType.LEFT, CellType.LEFT, CellType.LEFT}
    };
    List<Poi> pois = new ArrayList<>();
    pois.add(new Poi(1L, "test", new SimplePoint(0, 0), PoiType.LOAD));
    pois.add(new Poi(2L, "the second", new SimplePoint(2, 1), PoiType.UNLOAD));
    WarehouseMap map = new WarehouseMap(arr, pois, new StrategyBreadthFirst());

    assertEquals("MAP,4,4,6331100116015555;", map.toString());
    assertEquals("POI,2,0,0,0,1,test,2,1,1,2,the second;", map.poisToString());
  }

  @Test
  public void testGetClosestPoi() {
    CellType[][] arr = {
      {CellType.POI, CellType.POI, CellType.RIGHT, CellType.NEUTRAL},
      {CellType.NEUTRAL, CellType.OBSTACLE, CellType.NEUTRAL, CellType.NEUTRAL},
      {CellType.NEUTRAL, CellType.POI, CellType.POI, CellType.POI},
      {CellType.LEFT, CellType.POI, CellType.LEFT, CellType.LEFT}
    };
    List<Poi> pois = new ArrayList<>();
    pois.add(new Poi(1L, "test", new SimplePoint(0, 0), PoiType.EXIT));
    pois.add(new Poi(2L, "the second", new SimplePoint(2, 1), PoiType.EXIT));
    pois.add(new Poi(3L, "the second", new SimplePoint(2, 2), PoiType.EXIT));
    pois.add(new Poi(4L, "the second", new SimplePoint(0, 1), PoiType.LOAD));
    pois.add(new Poi(5L, "the second", new SimplePoint(3, 1), PoiType.UNLOAD));
    pois.add(new Poi(6L, "the second", new SimplePoint(2, 3), PoiType.LOAD));
    WarehouseMap map = new WarehouseMap(arr, pois, new StrategyBreadthFirst());

    Position p1 = new Position(1, 0, Orientation.DOWN);
    Position p2 = new Position(0, 3, Orientation.UP);

    assertEquals(1L, map.getClosestExit(p1));
    assertEquals(3L, map.getClosestExit(p2));
  }
}
