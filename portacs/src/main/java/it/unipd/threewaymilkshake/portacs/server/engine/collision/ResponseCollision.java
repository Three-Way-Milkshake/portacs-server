/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import java.util.LinkedList;
import java.util.List;

public class ResponseCollision {
  private boolean recalculate;
  private boolean stop;
  private int numberOfStops;
  private boolean collisionOccurred;
  private boolean criticalRecalculate;
  private List<SimplePoint> obstacles;

  public List<SimplePoint> getObstacles() {
    return obstacles;
  }

  public ResponseCollision() {
    this.recalculate = false;
    this.criticalRecalculate = false;
    this.stop = false;
    this.numberOfStops = 0;
    this.collisionOccurred = false;
    this.obstacles = new LinkedList<>();
  }

  public boolean isInStop() {
    return this.stop;
  }

  public boolean isRecalculating() {
    return this.recalculate || this.criticalRecalculate;
  }

  public boolean isCriticalRecalculating() {
    return this.criticalRecalculate;
  }

  public void addStop(int numberOfStops) {
    this.numberOfStops += numberOfStops;
    stop = true;
  }

  public void setRecalculate(SimplePoint obstacle) {
    if (recalculate == false) {
      this.obstacles.add(obstacle);
    }
    recalculate = true;
  }

  public void setCriticalRecalculate() {
    criticalRecalculate = true;
  }

  public void setRecalculate(List<SimplePoint> obstacles) {
    this.obstacles = obstacles;
    recalculate = true;
  }

  public int getNumberOfStops() {
    return numberOfStops;
  }

  public void collisionOccured() {
    this.collisionOccurred = true;
  }

  public boolean hasCollisionOccurred() {
    return collisionOccurred;
  }
}
