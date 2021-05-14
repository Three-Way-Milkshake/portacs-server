/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;

import java.util.Arrays;
import java.util.List;

public class DeadlockCheck implements Handler<List<CollisionForklift>, List<CollisionForklift>> {

  @Override
  public List<CollisionForklift> process(List<CollisionForklift> input) {
    int ALERT_DEADLOCK = 3; // causa un ricalcolo del muletto
    int CRITICAL_DEADLOCK = 6; // invio evento eccezionale

    for (CollisionForklift f : input) {
      Forklift forklift = f.getForklift();
      if (forklift.isInDeadlock(ALERT_DEADLOCK)) { // TODO: Nicolò
        System.out.println("DEADLOCK ALERT! " + forklift.getId() + " has been in stall for " + forklift.getNumberOfStalls() + " turns");
        SimplePoint positionForklift = forklift.getPosition().getPoint();
        SimplePoint obstacle = forklift.getPosition().getPoint();

        for (int i = 1; obstacle.equals(positionForklift); i++) {
          obstacle = forklift.getNextPositions(i).get(i);
        }

        SimplePoint randomObstacle =
            forklift.getPosition().generateNearRandomPoint(positionForklift);

        System.out.println(
            "Calculating new path with obstacle at "
                + obstacle.getX()
                + ";"
                + obstacle.getY()
                + " and random point: "
                + randomObstacle.getX()
                + ";"
                + randomObstacle.getY());
        forklift.setDeadlock(false);
        f.setRecalculate(Arrays.asList(obstacle, randomObstacle));

      } else if (forklift.isInDeadlock(CRITICAL_DEADLOCK)) { // TODO: Nicolò
        forklift.addExceptionalEvent("Il muletto " + forklift.getId() +" è in stallo. Probabilmente è richiesto l'intervento dell'operatore");
      }
    }
    return input;
  }
}
