/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import java.util.Arrays;
import java.util.List;

public class DeadlockCheck implements Handler<List<CollisionForklift>, List<CollisionForklift>> {

  @Override
  public List<CollisionForklift> process(List<CollisionForklift> input) {
    int SIMPLE_DEADLOCK = 2; // causa un ricalcolo del muletto
    int CRITICAL_DEADLOCK = 4; // ricalcolo casuale
    int FATAL_DEADLOCK = 10;

    for (CollisionForklift f : input) {
      if(f.getForklift().isInDeadlock(CRITICAL_DEADLOCK)) {
        System.out.println("CRITICAL DEADLOCK! Unit " + f.getForklift().getId() + " has been in stall for " + f.getForklift().getNumberOfStalls() + " turns");
        f.setCriticalRecalculate();
      }
      else if (f.getForklift().isInDeadlock(SIMPLE_DEADLOCK)) { // TODO: Nicolò
        System.out.println("DEADLOCK ALERT! Unit " + f.getForklift().getId() + " has been in stall for " + f.getForklift().getNumberOfStalls() + " turns");
        SimplePoint positionForklift = f.getForklift().getPosition().getPoint();
        SimplePoint obstacle = f.getForklift().getPosition().getPoint();

        for (int i = 1; obstacle.equals(positionForklift); i++) {
          obstacle = f.getForklift().getNextPositions(i).get(i);
        }

        SimplePoint randomObstacle =
            f.getForklift().getPosition().generateNearRandomPoint(positionForklift);

        System.out.println(
            "Calculating new path with obstacle at "
                + obstacle.getX()
                + ";"
                + obstacle.getY()
                + " and random point at: "
                + randomObstacle.getX()
                + ";"
                + randomObstacle.getY());
        //f.getForklift().setDeadlock(false);
        f.setRecalculate(Arrays.asList(obstacle, randomObstacle));

        // f.setDeadlock(false);
        // SEGNALARE RICALCOLO -> PROBLEMA: NON DEVE ESSERE SOVRASCRITTO DA UN EVENTUALE
        // ALTRO MESSAGGIO DERIVANTE DALLA GESTIONE DELLE COLLISIONI (STOP/RICALCOLO)
        // OPPURE SMINCHIARE IL BUFFER
      }
    }
    
    return input;
  }
}
