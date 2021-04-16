package it.unipd.threewaymilkshake.portacs.server.engine.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

import it.unipd.threewaymilkshake.portacs.server.engine.Subject;
import it.unipd.threewaymilkshake.portacs.server.persistency.MapDao;

public class WarehouseMap extends Subject{

  private CellType[][] map;
  private Map<Long, Poi> pois;
  @Expose(serialize = false, deserialize = false) private PathFindingStrategy strategy;
  @Expose(serialize = false, deserialize = false) private MapDao mapDao;

  public WarehouseMap(CellType[][] map, List<Poi> pois, PathFindingStrategy pathFindingStrategy) {
    this.map = map;
    this.strategy=pathFindingStrategy;
    this.pois=new HashMap<>();
    pois.stream().forEach(p->{
      this.pois.put(p.getId(), p);
    });
  }

  /*public WarehouseMap(MapDao mapDao) {
    this.mapDao=mapDao;
    this.map = mapDao.readMapStructure();
    List<Poi> poisList=mapDao.readPois();
    this.pois=new HashMap<>();
    poisList.stream().forEach(p->{
      this.pois.put(p.getId(), p);
    });
  }*/

  private WarehouseMap(WarehouseMap m) {
    this.map = m.map;
    this.pois = m.pois;
  }

  public WarehouseMap(MapDao mapDao) {
    this(mapDao.readMap());
    this.mapDao=mapDao;
  }

  public void setStrategy(PathFindingStrategy strategy){
    this.strategy=strategy;
  }

  public String toString(){
    StringBuilder b=new StringBuilder();
    b.append("MAP,");
    b.append(String.valueOf(map.length)); //rows
    b.append(","+String.valueOf(map[0].length)+","); //cols
		Arrays.stream(map)
			.forEach(r->{
        //Arrays.stream(r).forEach(c->{c.ordinal();});
				b.append(String.valueOf(r));
		});
    b.append(";");
		return b.toString();
  }

}