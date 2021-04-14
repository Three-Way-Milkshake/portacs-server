package it.unipd.threewaymilkshake.portacs.server.engine.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unipd.threewaymilkshake.portacs.server.persistency.MapDao;

public class WarehouseMap{
  private static WarehouseMap instance;

  private CellType[][] map;
  private Map<Long, Poi> pois;
  public WarehouseMap(CellType[][] map, List<Poi> pois) {
    this.map = map;
    this.pois=new HashMap<>();
    pois.stream().forEach(p->{
      this.pois.put(p.getId(), p);
    });
  }

  public WarehouseMap(MapDao mapDao) {
    this.map = mapDao.readMapStructure();
    List<Poi> poisList=mapDao.readPois();
    this.pois=new HashMap<>();
    poisList.stream().forEach(p->{
      this.pois.put(p.getId(), p);
    });
  }

  /* public static WarehouseMap getInstance(){
    if(instance==null){
      instance=new WarehouseMap(MapDao, pois)
    }
  } */
}