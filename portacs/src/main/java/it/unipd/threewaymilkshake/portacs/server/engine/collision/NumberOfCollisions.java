/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberOfCollisions
    implements Handler<
        Map<SimplePoint, List<CollisionForklift>>, Map<SimplePoint, List<CollisionForklift>>> {

  @Override
  public Map<SimplePoint, List<CollisionForklift>> process(
      Map<SimplePoint, List<CollisionForklift>> collisions) {

    if (!collisions.isEmpty()) System.out.println("--RILEVATA COLLISIONE--");
    for (SimplePoint key : collisions.keySet()) {
      System.out.println("Nel punto: " + key.getX() + "," + key.getY() + "\n Unità coinvolte:");

      for (CollisionForklift unit : collisions.get(key)) {
        System.out.print(unit.getForklift().getId() + " ");
      }
      System.out.print("\n");
    }

    Map<CollisionForklift, Integer> numberOfCollision = new HashMap<CollisionForklift, Integer>();

    for (SimplePoint key : collisions.keySet()) {
      for (CollisionForklift unit : collisions.get(key)) {
        Integer j = numberOfCollision.get(unit);
        numberOfCollision.put(unit, (j == null) ? 1 : j + 1);
      }
    }

    for (CollisionForklift key : numberOfCollision.keySet()) {
      // System.out.println(key.id + ":" + numberOfCollision.get(key));

      if (numberOfCollision.get(key) > 1) {
        System.out.println(
            "Added 1 stop to unit"
                + key.getForklift().getId()
                + " because it causes > 1 collisions");
        key.addStop();
      }
    }

    return collisions;
  }
}
