/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.ForkliftDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

public class ForkliftsList {
  private Map<String, Forklift> forkliftsMap;
  private ForkliftDao forkliftDao;

  @Autowired private WarehouseMap warehouseMap;

  private static final String UNRECOGNIZED_FORKLIFT = "FAILED; Unrecognized forklift";
  private static final String WRONG_TOKEN = "FAILED; Wrong token";

  public ForkliftsList(ForkliftDao forkliftDao) {
    this.forkliftDao = forkliftDao;
    List<Forklift> forklifts = forkliftDao.readForklifts();
    forkliftsMap = new HashMap<>();
    forklifts.stream()
        .forEach(
            f -> {
              forkliftsMap.put(f.getId(), f);
            });
  }

  public boolean auth(Connection c) {
    boolean success = false;
    String id = c.read();
    String token = c.read();
    Forklift f = forkliftsMap.get(id);
    if (f != null) { // muletto esiste
      if (f.authenticate(token)) {
        success = true;
        f.bindConnection(c);
        f.write(warehouseMap.toString());
        f.writeAndSend(warehouseMap.poisToString());
      } else {
        c.send(WRONG_TOKEN);
        c.close();
      }
    } else {
      c.send(UNRECOGNIZED_FORKLIFT);
      c.close();
    }
    return success;
  }

  public List<Forklift> getActiveForklifts() {
    return forkliftsMap.values().stream().filter(f -> f.isActive()).collect(Collectors.toList());
  }

  public String getForkliftsPositions() {
    StringBuilder b = new StringBuilder();
    b.append("UNI,");
    b.append(forkliftsMap.size());
    b.append(',');
    forkliftsMap.values().stream()
        .forEach(
            f -> {
              b.append(f.getId());
              b.append(',');
              b.append(f.getPositionString());
              b.append(',');
            });
    b.deleteCharAt(b.length() - 1);
    b.append(';');

    return b.toString();
  }

  /** @return sequence representng all forklifts and their tasks (LIST,IDF,N,IDP1,IDP2;LIST...) */
  public String getForkliftsTasks() {
    StringBuilder b = new StringBuilder();
    forkliftsMap.forEach(
        (k, v) -> {
          b.append("LIST,");
          b.append(k);
          b.append(',');
          b.append(v.getTasksString());
          b.append(';');
        });
    return b.toString();
  }

  public String getForkliftsAndTokensString() {
    StringBuilder b = new StringBuilder();
    b.append("LISTF,");
    b.append(forkliftsMap.size());
    b.append(',');
    forkliftsMap.forEach(
        (k, v) -> {
          b.append(k);
          b.append(',');
          b.append(v.getToken());
          b.append(',');
        });

    b.deleteCharAt(b.length() - 1);
    b.append(';');

    return b.toString();
  }

  public Map<String, List<SimplePoint>> getAllNextPositions(int i) {
    Map<String, List<SimplePoint>> toReturn = new HashMap<String, List<SimplePoint>>();
    for (String key : forkliftsMap.keySet()) {
      toReturn.put(key, forkliftsMap.get(key).getNextPositions(i));
    }
    return toReturn;
  }
}
