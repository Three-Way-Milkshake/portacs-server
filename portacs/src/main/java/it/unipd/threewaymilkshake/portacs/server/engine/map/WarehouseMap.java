/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.map;

import com.google.gson.annotations.Expose;
import it.unipd.threewaymilkshake.portacs.server.engine.AbstractLocation;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.persistency.MapDao;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rappresenta la planimetria del magazzino ed espone un metodo per il calcolo automatico del
 * percorso PAR: X ATT: Y
 */
public class WarehouseMap {
  private PropertyChangeSupport support = new PropertyChangeSupport(this);
  @Expose private CellType[][] map;
  private int[][] intMatrix;
  @Expose private Map<Long, Poi> pois;
  private PathFindingStrategy strategy;
  private MapDao mapDao;

  public WarehouseMap(CellType[][] map, List<Poi> pois, PathFindingStrategy pathFindingStrategy) {
    this.map = map;
    updateIntMatrix();
    this.strategy = pathFindingStrategy;
    this.pois = new HashMap<>();
    pois.stream()
        .forEach(
            p -> {
              this.pois.put(p.getId(), p);
            });
  }

  // public WarehouseMap(MapDao mapDao) {
  private void updateIntMatrix() {
    int rows = map.length, cols = map[0].length;
    intMatrix = new int[rows][cols];
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        intMatrix[i][j] = map[i][j].ordinal();
      }
    }
  }

  int[][] getIntMatrix() {
    int rows = map.length, cols = map[0].length;
    int[][] r = new int[rows][cols];
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        r[i][j] = map[i][j].ordinal();
      }
    }

    return r;
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

  public WarehouseMap(MapDao mapDao, PathFindingStrategy pathFindingStrategy) {
    this(mapDao.readMap());
    this.mapDao = mapDao;
  }

  public List<Move> getPath(AbstractLocation start, long poi) {
    AbstractLocation end = pois.get(poi).getLocation();
    // return strategy.getPath(intMatrix, start, end);
    return strategy.getPath(getIntMatrix(), start, end);
  }

  public void setStrategy(PathFindingStrategy strategy) {
    this.strategy = strategy;
  }

  /** @return the map as Three Way protocol (MAP,R,C,SEQ...) */
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append("MAP,");
    b.append(String.valueOf(map.length)); // rows
    b.append("," + String.valueOf(map[0].length) + ","); // cols
    Arrays.stream(map)
        .forEach(
            r -> {
              Arrays.stream(r)
                  .forEach(
                      c -> {
                        b.append(c.ordinal());
                      });
            });
    b.append(";");
    return b.toString();
  }

  public String poisToString() {
    StringBuilder b = new StringBuilder();

    b.append("POI,");
    b.append(pois.size());
    b.append(',');
    pois.forEach(
        (k, v) -> {
          AbstractLocation l = v.getLocation();
          b.append(l.getX());
          b.append(',');
          b.append(l.getY());
          b.append(',');
          b.append(v.getType().ordinal());
          b.append(',');
          b.append(k);
          b.append(',');
          b.append(v.getName() + ',');
        });
    b.deleteCharAt(b.length() - 1);
    b.append(';');

    return b.toString();
  }

  public CellType[][] getMap() {
    return map;
  }

  public void setMap(CellType[][] map) {
    support.firePropertyChange("map", this.map, map);
    this.map = map;
  }

  public void setCell(int x, int y, String... actions){
    //CELL,X,Y,A[,ID,T,NAME]
    support.firePropertyChange("map", this.map, map);

    CellType type=CellType.values()[Integer.valueOf(actions[0])];
    if(map[x][y]==CellType.POI || type==CellType.POI){
      long poiId=Long.parseLong(actions[1]);
      if(type!=CellType.POI){
        pois.remove(poiId);
      }
      else if(map[x][y]==CellType.POI){
        Poi p=pois.get(poiId);
        p.setType(PoiType.values()[Integer.parseInt(actions[2])]);
        p.setName(actions[3]);
      }
      else{
        Poi p=new Poi(getNextPoiId(), actions[3], new SimplePoint(x, y), PoiType.values()[Integer.parseInt(actions[2])]);
      }
    }

    map[x][y]=type;
  }

  private long getNextPoiId(){
    return pois.keySet().stream().max(Long::compareTo).orElse(1L);
  }

  public Map<Long, Poi> getPois() {
    return pois;
  }

  public void setPois(Map<Long, Poi> pois) {
    support.firePropertyChange("map", this.map, map); //TODO is it right?
    this.pois = pois;
  }

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    support.removePropertyChangeListener(pcl);
  }

  public int getRows() {
    return map.length;
  }

  public int getColumns() {
    return map[0].length;
  }  
}
