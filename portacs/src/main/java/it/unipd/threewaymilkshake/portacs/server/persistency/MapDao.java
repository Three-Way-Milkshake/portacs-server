package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.util.List;

import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import it.unipd.threewaymilkshake.portacs.server.engine.map.Poi;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

public interface MapDao{
  void updateMap(WarehouseMap m);
  WarehouseMap readMap();
  CellType[][] readMapStructure();
  List<Poi> readPois();
}