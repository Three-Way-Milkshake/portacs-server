/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import java.util.List;

public class CollisionForklift {
  private Forklift forklift;
  private ResponseCollision response;

  public CollisionForklift(Forklift forklift) {
    this.forklift = forklift;
    this.response = new ResponseCollision();
  }

  public void addStop(int numberOfStops) {
    response.addStop(numberOfStops);
  }

  public void addStop() {
    response.addStop(1);
  }

  public Forklift getForklift() {
    return forklift;
  }

  public boolean isInStop() {
    return response.isInStop();
  }

  public boolean isRecalculating() {
    return response.isRecalculating();
  }

  public void setRecalculate(Position obstacle) {
    response.setRecalculate(obstacle.getPoint());
  }

  public boolean isCriticalRecalculating() {
    return response.isCriticalRecalculating();
  }

  public void setCriticalRecalculate() {
    response.setCriticalRecalculate();
  }

  public void setRecalculate(List<SimplePoint> obstacles) {
    response.setRecalculate(obstacles);
  }

  public List<SimplePoint> getObstacles() {
    return response.getObstacles();
  }

  public int getStops() {
    return response.getNumberOfStops();
  }

  public boolean hasCollisionOccurred() {
    return response.hasCollisionOccurred();
  }

  public void collisionOccurred() {
    response.collisionOccured();
  }
}

/**
 * 0- List<CollisionForklift> -> DeadlockCheck 1- List<CollisionForklift> -> CollisioDetection 2-
 * Map<SimplePoint,List<CollisionForklift>> -> NumberOfCollisions 3-
 * Map<SimplePoint,List<CollisionForklift>> -> HeadOnCollision 4-
 * Map<SimplePoint,List<CollisionForklift>> -> NearestToCollision 5- List<CollisionForklift>
 */
