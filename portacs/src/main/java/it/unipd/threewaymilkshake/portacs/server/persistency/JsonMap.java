package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import it.unipd.threewaymilkshake.portacs.server.engine.map.Poi;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

public class JsonMap implements MapDao{

  private String filePath;

  public JsonMap(String filePath){
    this.filePath=filePath;
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
    return null;
    /*Gson gson=new Gson();
    char[][] arr=null;
    try{
      arr=gson.fromJson(new FileReader(filePath), char[][].class);
    }
    catch(FileNotFoundException e){
      System.out.println("The file "+filePath+" does not exist!");
      e.printStackTrace();
    }*/
  }

  @Override
  public List<Poi> readPois() {
    // TODO Auto-generated method stub
    return null;
  }

}