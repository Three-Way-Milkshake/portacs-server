package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.persistency.ForkliftDao;

public class ForkliftsList {
  private Map<String, Forklift> forkliftsMap;
  private ForkliftDao forkliftDao;

  private final static String UNRECOGNIZED_FORKLIFT = "FAILED; Unrecognized forklift";
  private final static String WRONG_TOKEN = "FAILED; Wrong token";

  public ForkliftsList(ForkliftDao forkliftDao) {
    this.forkliftDao = forkliftDao;
    List<Forklift> forklifts = forkliftDao.readForklifts();
    forkliftsMap = new HashMap<>();
    forklifts.stream().forEach(f -> {
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

  public Map<String,SimplePoint> getAllNextPositions(int i) {

    Map<String,SimplePoint> toReturn = new HashMap<String,SimplePoint>();

        for(String key : forkliftsMap.keySet()) {
          //toReturn.put(key,forkliftsMap.get(key).getNextPosition(i));
        }
    return toReturn;
  }
}