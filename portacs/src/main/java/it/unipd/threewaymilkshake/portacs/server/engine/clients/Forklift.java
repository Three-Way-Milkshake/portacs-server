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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

public class Forklift extends Client {
  @Expose String token;
  private TasksSequence tasks;
  private List<Move> pathToNextTask;
  private Position position;
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

  public void setPosition(Position position) { // TODO: visibility
    this.position = position;
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
                    updatePosition(par);
                    System.out.println("I am at: " + position.toString());
                    break;
                  case "PATH":
                    if (par[1].equals("1")) tasks.extractNext();
                    connection.writeToBuffer("PATH," + getPathToNextTask() + ";");
                    break;
                  case "LIST":
                    if(tasks==null || tasks.isEmpty()){
                      tasks = tasksSequencesList.getTasksSequence();
                    }
                    connection.writeToBuffer(tasks.toString());
                    break;
                  default:
                    System.out.println("Unrecognized message: " + par[0]);
                }
              });
    } else {
      clearConnection();
    }
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

  public String getPathToNextTask(SimplePoint point) {
    pathToNextTask = warehouseMap.getPath(position, tasks.getNext(), point);
    return pathToNextTask.stream()
      .map(m->m.ordinal())
      .collect(Collectors.toList())
      .toString()
      .replaceAll("\\[|\\]| ", "");
    // return pathToNextTask.toString().replaceAll("\\[|\\]", "");
  }

  private void updatePosition(String... pos) {
    position.setPosition(
        Integer.parseInt(pos[1]),
        Integer.parseInt(pos[2]),
        Orientation.values()[Integer.parseInt(pos[3])]);
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

}
