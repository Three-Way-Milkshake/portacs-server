/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

public interface MapDao {
  void updateMap(WarehouseMap m);

  WarehouseMap readMap();
}
