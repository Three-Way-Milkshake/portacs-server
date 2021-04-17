package it.unipd.threewaymilkshake.portacs.server.engine.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

import it.unipd.threewaymilkshake.portacs.server.engine.AbstractLocation;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Subject;
import it.unipd.threewaymilkshake.portacs.server.persistency.MapDao;

public class WarehouseMap extends Subject{

  private CellType[][] map;
  @Expose(serialize = false, deserialize = false) private int[][] intMatrix;
  private Map<Long, Poi> pois;
  @Expose(serialize = false, deserialize = false) private PathFindingStrategy strategy;
  @Expose(serialize = false, deserialize = false) private MapDao mapDao;

  public WarehouseMap(CellType[][] map, List<Poi> pois, PathFindingStrategy pathFindingStrategy) {
    this.map = map;
    updateIntMatrix();
    this.strategy=pathFindingStrategy;
    this.pois=new HashMap<>();
    pois.stream().forEach(p->{
      this.pois.put(p.getId(), p);
    });
  }

  //public WarehouseMap(MapDao mapDao) {
  private void updateIntMatrix(){
    int rows=map.length, cols=map[0].length;
    intMatrix=new int[rows][cols];
    for(int i=0; i<rows; ++i){
      for(int j=0; j<cols; ++j){
        intMatrix[i][j]=map[i][j].ordinal();
      }
    }
  }

  /*public WarehouseMap(MapDao mapDao, PathFindingStrategy pathFindingStrategy) {
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

  public WarehouseMap(MapDao mapDao, PathFindingStrategy pathFindingStrategy){
    this(mapDao.readMap());
    this.mapDao=mapDao;
  }

  public List<Move> getPath(AbstractLocation start, long poi){
    AbstractLocation end=pois.get(poi).getLocation();
    return strategy.getPath(intMatrix, start, end);
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