/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class NearestToCollision
    implements Handler<Map<SimplePoint, List<CollisionForklift>>, Set<CollisionForklift>> {

  @Override
  public Set<CollisionForklift> process(Map<SimplePoint, List<CollisionForklift>> collisions) {
    // System.out.println("CIAO");

    for (SimplePoint key : collisions.keySet()) {
      int min = Integer.MAX_VALUE;
      for (CollisionForklift unit : collisions.get(key)) {
        int distance = unit.getForklift().getPosition().getPoint().calculateDistance(key);
        // se distanza > 0 && !stop && !ricalcolo
        /*if (distance == 0 || !unit.isInStop() && !unit.isRecalculating()) {
            if (distance <= min)
                min = distance;
        }*/
        if (distance < min) min = distance;
      }
      List<CollisionForklift> equals = new LinkedList<CollisionForklift>();
      for (CollisionForklift unit : collisions.get(key)) {
        int distance = unit.getForklift().getPosition().getPoint().calculateDistance(key);
        if (!unit.isInStop() && !unit.isRecalculating()) {
          if (distance > min) {
            System.out.println(
                "Unit "
                    + unit.getForklift().getId()
                    + " stops because its distance to the collision point is "
                    + distance
                    + " which is < than "
                    + min);
            unit.addStop();
          } else if (distance == min) {
            equals.add(unit);
          }
        }
      }

      if (equals.size() > 1) {
        Random rand = new Random();
        int random = rand.nextInt(equals.size());
        for (int i = 0; i < equals.size(); i++) {
          if (i != random) {
            System.out.println(
                "Unit "
                    + equals.get(i).getForklift().getId()
                    + " stops because it has been randomly chosen for distance purposes");
            equals.get(i).addStop();
          }
        }
      }
    }

    Set<CollisionForklift> output = new LinkedHashSet<>();
    for (SimplePoint key : collisions.keySet()) {
      for (CollisionForklift unit : collisions.get(key)) {
        output.add(unit);
      }
    }

    if (!output.isEmpty()) System.out.println("++RISOLUZIONE COLLISIONI: ++");
    for (CollisionForklift unit : output) {
      System.out.println(
          unit.getForklift().getId()
              + ": Stop per "
              + unit.getStops()
              + " turni; ricalcolo_"
              + unit.isRecalculating());
    }

    return output;
  }
}
