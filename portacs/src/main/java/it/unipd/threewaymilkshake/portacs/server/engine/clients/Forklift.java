/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import com.google.gson.annotations.Expose;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequence;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequencesList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

public class Forklift extends Client {
  @Expose String token;
  private TasksSequence tasks;
  private List<Move> pathToNextTask;
  private Position position;
  //private Position foreseenPosition = new Position(-1,-1,Orientation.UP);
  private Deque<String> exceptionalEvents;
  private int numberOfStalls; //TODO: Nicolò
        // ad ogni ricezione della posizione bisogna controllare
        // se il la posizione vecchia è uguale alla posizione
        // nuova: se sì -> incrementare numberOfStalls
        // se no -> azzerarla
  

  // private TasksSequencesList tasksSequencesList;

  public Forklift(String id, String token) {
    super(id);
    this.token = token;
  }

  public Forklift(String id, String token, TasksSequencesList tasksSequencesList) {
    this(id, token);
    this.tasksSequencesList = tasksSequencesList;
  }

  public void setPathToNextTask(List<Move> pathToNextTask) { // TODO: visibility
    this.pathToNextTask = pathToNextTask;
  }

  public void initializeFields(){
    this.pathToNextTask=new LinkedList<>();
    this.position=new Position(0,1,Orientation.RIGHT);
  }

  public void setPosition(Position position) { // TODO: visibility
    this.position = position;
  }

  public void setExceptionalEvents(Deque<String> exceptionalEvents) {
    this.exceptionalEvents=exceptionalEvents;
  }

  @Override
  public void processCommunication() {
    connection.send("ALIVE;");
    if (active && connection.isAlive()) {
      String[] commands = connection.getLastMessage().split(";");
      Arrays.stream(commands)
          .forEach(
              c -> {
                String[] par = c.split(",");

                System.out.print(id + ") Command: " + par[0] + ", params: ");
                for (int i = 1; i < par.length; ++i) {
                  System.out.print(par[i] + " ");
                }
                System.out.println();

                switch (par[0]) {
                  case "POS":
                    updatePositionAndDeadlock(par);
                    /*if(position != foreseenPosition) {
                      System.out.println("***********POSIZIONE SI DISCOSTA DA QUANTO CALCOLATO************");
                      System.out.println("E': " + position.getX() + " " + position.getY() + " "+ position.getOrientation());
                      System.out.println("Ma doveva essere: " + foreseenPosition.getX() + " " + foreseenPosition.getY() + " "+ foreseenPosition.getOrientation());
                    }*/
                    System.out.println("I am at: " + position.toString());
                    //if(!pathToNextTask.isEmpty())pathToNextTask.remove(0);
                    break;
                  case "PATH":
                    if (par[1].equals("1")){
                      if(!tasks.isEmpty()){
                        tasks.extractNext();
                      } 
                    }
                    if(tasks.isEmpty()){
                      connection.writeToBuffer("PATH,EMPTY;");
                    }
                    else{
                      connection.writeToBuffer("PATH," + getPathToNextTask() + ";");
                    }
                    break;
                  case "LIST":
                    if(tasks==null || tasks.isEmpty()){
                      tasks = tasksSequencesList.getTasksSequence();
                      //write to users?
                    }
                    connection.writeToBuffer(tasks.toString());
                    break;

                  case "ECC":
                    exceptionalEvents.add("ECC,"+"problem from unit "+id+";");

                  default:
                    System.out.println("Unrecognized message: " + par[0]);
                }
              });
    } else {
      clearConnection();
    }
  }

  public void printNextMoves() {
    for(Move m : pathToNextTask) {
      System.out.print(" " + m + " ");
    }
  }
  
  List<Move> returnPathToNextTask() {
    return pathToNextTask;
  }

  String getPathToNextTask() {
    pathToNextTask = warehouseMap.getPath(position, tasks.getNext());
    return pathToNextTask.stream()
      .map(m->m.ordinal())
      .collect(Collectors.toList())
      .toString()
      .replaceAll("\\[|\\]| ", "");
    // return pathToNextTask.toString().replaceAll("\\[|\\]", "");
  }

  public String getPathToNextTaskWithObstacles(List<SimplePoint> points) {
    //pathToNextTask.add(0,Move.STOP); 
    //if(points.size() == 1) pathToNextTask.add(0,Move.STOP);
    String path=null;
    try{
      pathToNextTask = warehouseMap.getPath(position, tasks.getNext(), points);
      
    }
    catch(Exception e){
      //do nothing
      // pathToNextTask.clear(); //TODO capire se svuotare o no mosse
      // path=String.valueOf(Move.STOP.ordinal()+Move.STOP.ordinal());
      pathToNextTask.add(0, Move.STOP);
      pathToNextTask.add(0, Move.STOP);
    }

    path=pathToNextTask.stream()
      .map(m->m.ordinal())
      .collect(Collectors.toList())
      .toString()
      .replaceAll("\\[|\\]| ", "");

    System.out.println("CALCULATED PATH IS: "+pathToNextTask);

    return path;
    // return pathToNextTask.toString().replaceAll("\\[|\\]", "");
  }

  public String getPathToNextTaskWithObstacleRandom(SimplePoint obstacle,Position forkliftPosition) { //TODO: to remove
    //pathToNextTask.add(0,Move.STOP);
    return pathToNextTask.stream()
      .map(m->m.ordinal())
      .collect(Collectors.toList())
      .toString()
      .replaceAll("\\[|\\]| ", "");
    // return pathToNextTask.toString().replaceAll("\\[|\\]", "");
  }

  private void updatePositionAndDeadlock(String... pos) {
    Integer newX = Integer.parseInt(pos[1]);
    Integer newY = Integer.parseInt(pos[2]);
    Orientation newOrientation = Orientation.values()[Integer.parseInt(pos[3])];
    if(newX == position.getX() && newY == position.getY() && newOrientation == position.getOrientation() && hasPath()){
      numberOfStalls++;
    }
    else {
      position.setPosition(newX,newY,newOrientation);
      numberOfStalls = 0;
    }


    
  }

  void resetPosition(){
    position=new Position(0,0,Orientation.UP);
  }

  @Override
  public boolean authenticate(String s) {
    return s.equals(token);
  }

  public String getPositionString() {
    return position.toString();
  }

  /** @return next tasks number and ids only e.g.: given 3 tasks 1,2,3 will return: 3,1,2,3 */
  public String getTasksString() {
    return String.valueOf(tasks.size()) + ',' + tasks.toString().replaceAll("(LIST,|;)", "");
  }

  public String getToken() {
    return token;
  }

  public List<SimplePoint> getNextPositions(int numberOfNextMoves) {
    LinkedList<SimplePoint> positionsToReturn = new LinkedList<SimplePoint>();
    Position positionParameter =
        new Position(position.getX(), position.getY(), position.getOrientation());
    return getNextPositionsRecursive(
        0, numberOfNextMoves, positionParameter, pathToNextTask, positionsToReturn);
  }

  private static List<SimplePoint> getNextPositionsRecursive(int i, int numberOfNextMoves,Position actualPosition, List<Move> pathToNextTask, LinkedList<SimplePoint> toReturn) {
    
    if(i == numberOfNextMoves+1) {
      return toReturn;
    }
    else if(i == 0) {
      toReturn.add(new SimplePoint(actualPosition.getX(),actualPosition.getY()));
      return getNextPositionsRecursive(i+1,numberOfNextMoves,actualPosition, pathToNextTask, toReturn);
    }
    else {
      if(pathToNextTask.size() > i-1) {
        actualPosition.computeNextPosition(pathToNextTask.get(i-1));
      }
      toReturn.add(new SimplePoint(actualPosition.getX(),actualPosition.getY()));
      return getNextPositionsRecursive(i+1,numberOfNextMoves,actualPosition, pathToNextTask, toReturn);
    }
  }


  public Position getPosition() {
    return position;
  }

  public boolean isInDeadlock(int threshold) {
    return numberOfStalls >= threshold;
  }

  public void addMove(Move move) {
    pathToNextTask.add(0,move);
  }

  public void removeFirstMove() {
    if(!pathToNextTask.isEmpty())pathToNextTask.remove(0);
  }

  public void clearPath() {
    pathToNextTask.clear();
  }

  public boolean hasPath() {
    return !pathToNextTask.isEmpty();
  }

  public Move getNextMove() {
    return pathToNextTask.get(0);
  }

  public void setDeadlock(boolean b) {
    numberOfStalls = 0;
  }

  public String getCurrentPathString() {
    return pathToNextTask.stream()
      .map(m->m.ordinal())
      .collect(Collectors.toList())
      .toString()
      .replaceAll("\\[|\\]| ", "");
  }

  /*public void setForeseenPosition(Position foreseen) {
    foreseenPosition = foreseen;
  }*/


}
