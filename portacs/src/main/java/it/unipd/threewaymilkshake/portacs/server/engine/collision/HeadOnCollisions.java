/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HeadOnCollisions
    implements Handler<
        Map<SimplePoint, List<CollisionForklift>>, Map<SimplePoint, List<CollisionForklift>>> {

  public boolean headOnRisk(CollisionForklift first, CollisionForklift second) {
    Position positionFirst = first.getForklift().getPosition();
    Position positionSecond = second.getForklift().getPosition();
    if (positionFirst.getPoint().equals(positionSecond.getPoint())) {
      first.collisionOccurred();
      second.collisionOccurred();
    } else if (positionFirst.getX() == positionSecond.getX()) {
      if (positionFirst.getY() < positionSecond.getY()) {
        return positionFirst.getOrientation() == Orientation.RIGHT
            && positionSecond.getOrientation() == Orientation.LEFT;
      } else if (positionFirst.getY() > positionSecond.getY()) {
        return positionFirst.getOrientation() == Orientation.LEFT
            && positionSecond.getOrientation() == Orientation.RIGHT;
      }
    } else if (positionFirst.getY() == positionSecond.getY()) {
      if (positionFirst.getX() < positionSecond.getX()) {
        return positionFirst.getOrientation() == Orientation.DOWN
            && positionSecond.getOrientation() == Orientation.UP;
      } else if (positionFirst.getX() > positionSecond.getX()) {
        return positionFirst.getOrientation() == Orientation.UP
            && positionSecond.getOrientation() == Orientation.DOWN;
      }
    }
    return false;
  }

  public void setCollisions(CollisionForklift a, CollisionForklift b) {
    if (headOnRisk(a, b)) { // c'è rischio frontale
      Position positionA = a.getForklift().getPosition();
      Position positionB = b.getForklift().getPosition();
      System.out.println("Head on risk: ");
      if (a.isInStop()) {
        System.out.println(
            "Unit "
                + a.getForklift().getId()
                + " will stop, while "
                + b.getForklift().getId()
                + " will recalculateasdf based on the position "
                + positionA);
        a.addStop();
        b.setRecalculate(positionA);
      } else if (b.isInStop()) {
        System.out.println(
            "Unit "
                + b.getForklift().getId()
                + " will stop, while "
                + a.getForklift().getId()
                + " will recalculate based on the position "
                + positionB);
        b.addStop();
        a.setRecalculate(positionB);
      } else {
        Random rand = new Random();
        int random = rand.nextInt(2);
        if (random == 0) {
          System.out.println(
              "Unit "
                  + a.getForklift().getId()
                  + " will stop, while "
                  + b.getForklift().getId()
                  + " will recalculate based on the position "
                  + positionA);
          a.addStop();
          b.setRecalculate(positionA);
        } else {
          System.out.println(
              "Unit "
                  + b.getForklift().getId()
                  + " will stop, while "
                  + a.getForklift().getId()
                  + " will recalculate based on the position "
                  + positionB);
          b.addStop();
          a.setRecalculate(positionB);
        }
      }
    }
  }

  @Override
  public Map<SimplePoint, List<CollisionForklift>> process(
      Map<SimplePoint, List<CollisionForklift>> collisions) {

    for (SimplePoint key : collisions.keySet()) {
      for (int i = 0; i < collisions.get(key).size() - 1; i++) {
        for (int j = i + 1; j < collisions.get(key).size(); j++) {
          setCollisions(collisions.get(key).get(i), collisions.get(key).get(j));
        }
      }
    }

    return collisions;
  }
}
