/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import static it.unipd.threewaymilkshake.portacs.server.engine.map.CellType.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ClientTest {
  @Test
  public void testConnectionBindingAndActiveStatus() {
    Client c = mock(Client.class, Mockito.CALLS_REAL_METHODS);
    c.setWarehouseMap(mock(WarehouseMap.class));
    assertFalse(c.isActive());
    Connection conn = mock(Connection.class);
    c.bindConnection(conn);
    assertTrue(c.isActive());
    c.clearConnection();
    assertFalse(c.isActive());
  }

  /**
   * should be probably removed, already testing observer behaviour in warehousmap test, would need
   * to instatiate a real map here
   */
  @Test
  @Disabled
  public void testPropertyChangeMechanism() {
    Client c = mock(Client.class, Mockito.CALLS_REAL_METHODS);
    Connection conn = mock(Connection.class);
    c.bindConnection(conn);
    WarehouseMap map = mock(WarehouseMap.class, Mockito.CALLS_REAL_METHODS);
    CellType[][] arr = {
      {NEUTRAL, NEUTRAL},
      {OBSTACLE, OBSTACLE}
    };
    map.setMap(arr);
    map.addPropertyChangeListener(c);
    verify(c, times(1)).propertyChange(any());
  }

  @Test
  @Disabled // really need this?
  public void testSendingMapInfo() {
    Client c = mock(Client.class, Mockito.CALLS_REAL_METHODS);
    Connection conn = mock(Connection.class);
    c.bindConnection(conn);
  }
}
