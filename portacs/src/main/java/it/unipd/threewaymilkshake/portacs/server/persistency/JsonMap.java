package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.util.List;

import com.google.gson.Gson;

import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import it.unipd.threewaymilkshake.portacs.server.engine.map.Poi;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

public class JsonMap implements MapDao{

  private String filePath;

  public JsonMap(File f){
    this.filePath=filePath;
    Gson g=new Gson();
  }
  
  @Override
  public void updateMap(WarehouseMap m) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public WarehouseMap readMap() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CellType[][] readMapStructure() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Poi> readPois() {
    // TODO Auto-generated method stub
    return null;
  }

}