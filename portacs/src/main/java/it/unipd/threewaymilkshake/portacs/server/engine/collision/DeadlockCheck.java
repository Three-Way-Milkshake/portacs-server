/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import java.util.Arrays;
import java.util.List;

public class DeadlockCheck implements Handler<List<CollisionForklift>, List<CollisionForklift>> {

  @Override
  public List<CollisionForklift> process(List<CollisionForklift> input) {
    int SIMPLE_DEADLOCK = 2; // causa un ricalcolo del muletto
    int CRITICAL_DEADLOCK = 4; // ricalcolo casuale

    for (CollisionForklift f : input) {
      Forklift forklift = f.getForklift();
      if (forklift.isInDeadlock(CRITICAL_DEADLOCK)) {
        System.out.println(
            "CRITICAL DEADLOCK! Unit "
                + f.getForklift().getId()
                + " has been in stall for "
                + f.getForklift().getNumberOfStalls()
                + " turns");
        f.setCriticalRecalculate();
        forklift.setDeadlock(false);
      } else if (forklift.isInDeadlock(SIMPLE_DEADLOCK)) {
        System.out.println(
            "DEADLOCK ALERT! "
                + forklift.getId()
                + " has been in stall for "
                + forklift.getNumberOfStalls()
                + " turns");
        SimplePoint positionForklift = forklift.getPosition().getPoint();
        SimplePoint obstacle = forklift.getPosition().getPoint();
        System.out.println(
            "COMPARING "
                + obstacle.getX()
                + " "
                + obstacle.getY()
                + " e "
                + positionForklift.getX()
                + " "
                + positionForklift.getY());

        for (int i = 1; obstacle.equals(positionForklift); i++) {
          obstacle = forklift.getNextPositions(i).get(i);
        }

        SimplePoint randomObstacle = positionForklift.generateNearRandomPoint();

        System.out.println(
            "Calculating new path with obstacle at "
                + obstacle.getX()
                + ";"
                + obstacle.getY()
                + " and random point: "
                + randomObstacle.getX()
                + ";"
                + randomObstacle.getY());
        // forklift.setDeadlock(false);
        f.setRecalculate(Arrays.asList(obstacle, randomObstacle));
      }
    }
    return input;
  }
}
