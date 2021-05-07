/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequencesList;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionForklift;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.ForkliftDao;
import net.bytebuddy.utility.RandomString;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

public class ForkliftsList {
  private Map<String, Forklift> forkliftsMap;
  private ForkliftDao forkliftDao;

  @Autowired private WarehouseMap warehouseMap;
  @Autowired private TasksSequencesList tasksSequencesList;

  private static final String UNRECOGNIZED_FORKLIFT = "FAIL,Unrecognized forklift";
  private static final String WRONG_TOKEN = "FAIL,Wrong token";
  private static final String ACTIVE_FORKLIFT="FAIL,Forklift is active";
  private static final String ALREADY_EXISTING_FORKLIFT="FAIL,Forklift is active";

  private static final int TOKEN_LENGTH=16;

  public ForkliftsList(ForkliftDao forkliftDao, WarehouseMap warehouseMap, TasksSequencesList tasksSequencesList, Deque<String> exceptionalEvents) {
    this.forkliftDao = forkliftDao;
    List<Forklift> forklifts = forkliftDao.readForklifts();
    System.out.println("******************* REACHED ************* map is: "+warehouseMap);
    System.out.println("\n tasks are: "+tasksSequencesList);
    
    //TODO necessary? 
    this.warehouseMap=warehouseMap;
    this.tasksSequencesList=tasksSequencesList;

    forkliftsMap = new HashMap<>();
    forklifts.stream()
        .forEach(
            f -> {
              forkliftsMap.put(f.getId(), f);
              f.setWarehouseMap(warehouseMap);
              f.setTasksSequencesList(tasksSequencesList);
              f.resetPosition();
              f.setExceptionalEvents(exceptionalEvents);
            });

    System.out.println("Loaded forklifts are: "+getForkliftsAndTokensString());
  }

  public boolean auth(Connection c) {
    boolean success = false;
    String id = c.read();
    String token = c.read();
    System.out.println("authenticating: "+id+" with token: "+token);
    Forklift f = forkliftsMap.get(id);
    if (f != null) { // muletto esiste
      if (f.authenticate(token)) {
        success = true;
        f.bindConnection(c);
        f.write("OK;");
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

  public Forklift getForklift(String forkliftId){
    return forkliftsMap.get(forkliftId);
  }

  public List<Forklift> getActiveForklifts() {
    return forkliftsMap.values().stream().filter(f -> f.isActive()).collect(Collectors.toList());
  }

  String addForklift(String newId){
    String res="ADF,";
    if(forkliftsMap.containsKey(newId)){
      res+=ALREADY_EXISTING_FORKLIFT;
    }
    else{
      String token=generateRandomToken();
      Forklift f=new Forklift(newId, token);
      f.initializeFields();
      f.setWarehouseMap(warehouseMap);
      forkliftsMap.put(newId, f);
      res+="OK,"+token;
    }

    return res;
  }

  String removeForklift(String toRemoveId){
    String res="RMF,";
    if(!forkliftsMap.containsKey(toRemoveId)){
      res+=UNRECOGNIZED_FORKLIFT;
    }
    else if(forkliftsMap.get(toRemoveId).isActive()){
      res+=ACTIVE_FORKLIFT;
    }
    else{
      forkliftsMap.remove(toRemoveId);
      res+="OK";
    }

    return res;
  }

  String generateRandomToken() {
    /* return new Random(System.currentTimeMillis())
      .ints()
      .limit(TOKEN_LENGTH)
      .map(String::valueOf)
      .collect(Collectors.toList()); */
      return new RandomString(TOKEN_LENGTH).nextString();
      // return r.nextString();
      // .toString();
    // return "FAKETOKEN";
  }

  public String getForkliftsPositions() {
    StringBuilder b = new StringBuilder();
    b.append("UNI,");
    b.append(forkliftsMap.size());
    b.append(',');
    forkliftsMap.values().stream()
        .forEach(
            f -> {
              // if(f.isActive()){
                b.append(f.getId());
                b.append(',');
                b.append(f.getPositionString());
                b.append(',');
              // }
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

  /**
   * @return forklifts list as Three Way protocolo
   * (LISTF,N,ID1,T1,ID2,T2...)
   */
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
      if(getForklift(key).isActive()) {
        toReturn.put(key, forkliftsMap.get(key).getNextPositions(i));
      }
    }
    return toReturn;
  }

  /**
   * 
   * @param first id of first muletto
   * @param second id of second muletto
   * @return true iif there is a head-on risk (rischio di frontale) based on if the two units are horizontally or verically aligned
   */
  public boolean headOnRisk(String first, String second) { //TODO: migrating
    Position positionFirst = getPositionFromString(first);
    Position positionSecond = getPositionFromString(second);
        if(positionFirst.getX() == positionSecond.getX()) {
            if(positionFirst.getY() < positionSecond.getY()) {
                return positionFirst.getOrientation() == Orientation.RIGHT && positionSecond.getOrientation() == Orientation.LEFT;
            }
            else if(positionFirst.getY() > positionSecond.getY()) {
                return positionFirst.getOrientation() == Orientation.LEFT && positionSecond.getOrientation() == Orientation.RIGHT;
            }
        }
        else if(positionFirst.getY() == positionSecond.getY()) {
            if(positionFirst.getX() < positionSecond.getX()) {
                return positionFirst.getOrientation() == Orientation.DOWN && positionSecond.getOrientation() == Orientation.UP;
            }
            else if(positionFirst.getX() > positionSecond.getX()) {
                return positionFirst.getOrientation() == Orientation.UP && positionSecond.getOrientation() == Orientation.DOWN;
            }
        }
        return false;
    }
  

  public SimplePoint getSimplePointFromString(String a) {
    //TODO : migrating
    return new SimplePoint(forkliftsMap.get(a).getPosition().getX(),forkliftsMap.get(a).getPosition().getY());
  }

  public Position getPositionFromString(String a) {
    return forkliftsMap.get(a).getPosition();
  }

  public void runtimeDeadlockChecker() {
    int ALERT_DEADLOCK = 2; // causa un ricalcolo del muletto
    int CRITICAL_DEADLOCK = 10; // invio evento eccezionale

    for(String key : forkliftsMap.keySet()) 
    {
      Forklift f = forkliftsMap.get(key);
      if(f.isInDeadlock(ALERT_DEADLOCK)) { //TODO: Nicolò
      System.out.println("DEADLOCK ALERT!!!!!!!!!!! " + f.id);
      SimplePoint obstacle = new SimplePoint(f.getPosition().getX(),f.getPosition().getY());
      
      for(int i = 1; obstacle.getX() == f.getPosition().getX() && obstacle.getY() == f.getPosition().getY(); i++) {
        obstacle = f.getNextPositions(i).get(i);
      }
      System.out.println("Calculating new path with obstacle at " + obstacle.getX() + ";"+ obstacle.getY());
      String nextPath = f.getPathToNextTaskWithObstacleRandom(obstacle,f.getPosition());
      f.write("PATH," + nextPath + ";");

      //f.setDeadlock(false);
        // SEGNALARE RICALCOLO -> PROBLEMA: NON DEVE ESSERE SOVRASCRITTO DA UN EVENTUALE 
        // ALTRO MESSAGGIO DERIVANTE DALLA GESTIONE DELLE COLLISIONI (STOP/RICALCOLO) 
        // OPPURE SMINCHIARE IL BUFFER
      }
      else if(f.isInDeadlock(CRITICAL_DEADLOCK)) { //TODO: Nicolò
        // SEGNALARE EVENTO ECCEZIONALE
      }
    }
  }

  public void goWithNextMove() {
    getActiveForklifts().parallelStream().forEach(Forklift::removeFirstMove);
  
   /*  for(String key : forkliftsMap.keySet()) {
      Forklift f = forkliftsMap.get(key);
      Position foreseen = new Position(-1,-1,Orientation.UP);
      if(!f.returnPathToNextTask().isEmpty()) f.getPosition().computeNextPosition(f.returnPathToNextTask().get(0));
      f.setForeseenPosition(foreseen);
      if(f.isActive()) {f.removeFirstMove();}
    } */
  }

  public List<CollisionForklift> getCollisionForklifts() {
    return forkliftsMap.values().stream().filter(f -> f.isActive()).map(f -> new CollisionForklift(f)).collect(Collectors.toList());
  }

}
