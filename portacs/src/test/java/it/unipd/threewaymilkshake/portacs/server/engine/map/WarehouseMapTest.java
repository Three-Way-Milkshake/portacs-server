/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    pois.add(new Poi(1L, "test", new SimplePoint(0, 0)));
    WarehouseMap map = new WarehouseMap(arr, pois, null);
    assertEquals(arr, map.getMap());
    // User observer=new Manager("id", "firstName", "lastName", "pwdHash");
    User observer = mock(Manager.class);
    map.addPropertyChangeListener(observer);
    verifyNoInteractions(observer);
    map.setMap(arr2);
    verify(observer, times(1)).propertyChange(any());
    verifyNoMoreInteractions(observer);
  }

  @Test
  public void testMapCreationAndIntConversion(){
    CellType[][] arr={
      {CellType.RIGHT,     CellType.RIGHT,    CellType.RIGHT,    CellType.NEUTRAL},
      {CellType.NEUTRAL,   CellType.OBSTACLE, CellType.OBSTACLE, CellType.NEUTRAL},
      {CellType.NEUTRAL,   CellType.OBSTACLE, CellType.OBSTACLE, CellType.NEUTRAL},
      {CellType.LEFT,      CellType.LEFT,     CellType.LEFT,     CellType.LEFT}
    };
    List<Poi> pois = new ArrayList<>();
    pois.add(new Poi(1L, "test", new SimplePoint(0, 0)));
    WarehouseMap map=new WarehouseMap(arr, pois, new StrategyBreadthFirst());

    int[][] expected={
      {3,3,3,1},
      {1,0,0,1},
      {1,0,0,1},
      {5,5,5,5}
    };
    int[][] actual=map.getIntMatrix();
    assertArrayEquals(expected, actual);
  }
}
