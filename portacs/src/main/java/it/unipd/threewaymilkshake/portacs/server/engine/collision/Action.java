/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import java.util.Collections;
import java.util.List;

public class Action {
  private List<String> actions; // STOP o RICALCOLO
  public SimplePoint obstacle;

  public void add(String toAdd) {
    actions.add(toAdd);
  }

  public Action(List<String> actions) {
    this.actions = actions;
    this.obstacle = null;
  }

  public boolean isEmpty() {
    return actions.isEmpty();
  }

  public boolean needRecalculation() {
    return actions.contains("RICALCOLO");
  }

  public SimplePoint getObstacle() {
    return obstacle;
  }

  public int stopCount() {
    return Collections.frequency(actions, "STOP");
  }

  public boolean isInStop() {
    boolean found = false;
    for (String action : actions) {
      if (action == "STOP") found = true;
    }
    return found;
  }

  public boolean isCalculatingAgaing() {
    boolean found = false;
    for (String action : actions) {
      if (action == "RICALCOLO") found = true;
    }
    return found;
  }

  public void printList() {
    for (String action : actions) {
      System.out.printf("---" + action + " ");
    }
    System.out.printf(
        "---" + ((obstacle == null) ? " / " : obstacle.getX() + " " + obstacle.getY()));
  }
}
