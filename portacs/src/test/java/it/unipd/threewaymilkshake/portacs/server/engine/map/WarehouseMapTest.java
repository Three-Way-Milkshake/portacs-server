package it.unipd.threewaymilkshake.portacs.server.engine.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Manager;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class WarehouseMapTest {

  @Test
  public void testPropertyChangeMechanism(){
    CellType[][] arr=new CellType[][]{
      {CellType.OBSTACLE, CellType.OBSTACLE},
      {CellType.NEUTRAL, CellType.NEUTRAL}
    };
    CellType[][] arr2=new CellType[][]{
      {CellType.NEUTRAL, CellType.NEUTRAL},
      {CellType.NEUTRAL, CellType.NEUTRAL}
    };
    List<Poi> pois=new ArrayList<>();
    pois.add(new Poi(1L, "test", new SimplePoint(0,0)));
    WarehouseMap map=new WarehouseMap(arr, pois, null);
    assertEquals(arr, map.getMap());
    //User observer=new Manager("id", "firstName", "lastName", "pwdHash");
    User observer=mock(Manager.class);
    map.addPropertyChangeListener(observer);
    verifyNoInteractions(observer);
    map.setMap(arr2);
    verify(observer, times(1)).propertyChange(any());
    verifyNoMoreInteractions(observer); 
  }
}
