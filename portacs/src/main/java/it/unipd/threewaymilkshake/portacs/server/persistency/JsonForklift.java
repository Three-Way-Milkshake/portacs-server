package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.util.List;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;

public class JsonForklift implements ForkliftDao{

  private String filePath;

  public JsonForklift(String filePath){
    this.filePath=filePath;
  }

  @Override
  public void addForklift(Forklift f) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Forklift> readfForklifts() {
    // TODO Auto-generated method stub
    return null;
  }

}