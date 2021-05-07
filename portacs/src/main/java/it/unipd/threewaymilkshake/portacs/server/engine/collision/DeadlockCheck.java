package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;

public class DeadlockCheck
        implements Handler<List<CollisionForklift>, List<CollisionForklift>> {

    @Override
    public List<CollisionForklift> process(List<CollisionForklift> input) {
        int ALERT_DEADLOCK = 3; // causa un ricalcolo del muletto
        int CRITICAL_DEADLOCK = 10; // invio evento eccezionale

        for (CollisionForklift f : input) {
            if (f.getForklift().isInDeadlock(ALERT_DEADLOCK)) { // TODO: Nicolò
                System.out.println("DEADLOCK ALERT!!! " + f.getForklift().getId());
                SimplePoint positionForklift = f.getForklift().getPosition().getPoint();
                SimplePoint obstacle = f.getForklift().getPosition().getPoint();

                for (int i = 1; obstacle == positionForklift; i++) {
                    obstacle = f.getForklift().getNextPositions(i).get(i);
                }

                SimplePoint randomObstacle = f.getForklift().getPosition().generateNearRandomPoint(positionForklift);

                System.out.println("Calculating new path with obstacle at " + obstacle.getX() + ";" + obstacle.getY() + "and random point: " + randomObstacle.getX() + ";" + randomObstacle.getY());
                f.getForklift().setDeadlock(false);
                f.setRecalculate(Arrays.asList(obstacle,randomObstacle));
                

                // f.setDeadlock(false);
                // SEGNALARE RICALCOLO -> PROBLEMA: NON DEVE ESSERE SOVRASCRITTO DA UN EVENTUALE
                // ALTRO MESSAGGIO DERIVANTE DALLA GESTIONE DELLE COLLISIONI (STOP/RICALCOLO)
                // OPPURE SMINCHIARE IL BUFFER
            } else if (f.getForklift().isInDeadlock(CRITICAL_DEADLOCK)) { // TODO: Nicolò
                // SEGNALARE EVENTO ECCEZIONALE
            }
        }
        return input;
    }

}
