/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import java.util.List;

public class JsonForklift implements ForkliftDao {

  private String filePath;

  public JsonForklift(String filePath) {
    this.filePath = filePath;
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
