/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.persistency.ForkliftDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForkliftsList {
  private Map<String, Forklift> forkliftsMap;
  private ForkliftDao forkliftDao;

  private static final String UNRECOGNIZED_FORKLIFT = "FAILED; Unrecognized forklift";
  private static final String WRONG_TOKEN = "FAILED; Wrong token";

  public ForkliftsList(ForkliftDao forkliftDao) {
    this.forkliftDao = forkliftDao;
    List<Forklift> forklifts = forkliftDao.readfForklifts();
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
}
