/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.Client;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionForklift;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionPipeline;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Engine /* implements Runnable */ {
  /* @Override
  public void run() {
    int counter=0;
    while(true){
      System.out.println("Hello "+(counter++));
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  } */
  private static int counter = 0;

  @Autowired private UsersList usersList;

  @Autowired private ForkliftsList forkliftsList;

  @Autowired private WarehouseMap warehouseMap;

  @Autowired Deque<String> exceptionalEvents;

  @Autowired
  // CollisionPipeline<ForkliftsList,Map<String, Action>> collisionPipeline;
  CollisionPipeline<List<CollisionForklift>, Set<CollisionForklift>> collisionPipeline;

  // private CollisionDetector collisionDetector=new CollisionDetector();
  // private CollisionSolver collisionSolver=new CollisionSolver();

  /* CollisionPipeline<ForkliftsList,Map<String, Action>> collisionPipeline
  = new CollisionPipeline<>(new CollisionDetector().setWarehouseMap(warehouseMap))
  .addHandler(new CollisionSolver().setForkliftsList(forkliftsList)); */

  /*CollisionPipeline<ForkliftsList, Map<String, Action>> collisionPipeline =
  new CollisionPipeline<>(new CollisionDetector()
  .addHandler(new CollisionSolver()));*/

  @Scheduled(
      fixedDelay = 1000,
      initialDelay =
          3000) // TODO set fixedRate instead to skip eventual failed executions (blocked IO) (?)
  public void execute() {
    System.out.println("Hello from engine " + (counter++) /* +" with map"+warehouseMap */);
    System.out.println(
        "there are "
            + forkliftsList.getActiveForklifts().size()
            + " forklifts and "
            + usersList.getActiveUsers().size()
            + " users active");

    // FORKLIFT JOBS
    forkliftsList.getActiveForklifts().stream().parallel().forEach(Client::processCommunication);

    Set<CollisionForklift> response =
        collisionPipeline.execute(forkliftsList.getCollisionForklifts());

    response.forEach(
        (fork) -> {
          Forklift forklift = fork.getForklift();
          if (fork.hasCollisionOccurred()
              && !warehouseMap.isBasePOI(forklift.getPosition().getPoint())
              && forklift.isActive()) {
            System.out.println(
                "***********COLLISIONE AVVENUTA**********: unita' " + fork.getForklift().getId());
            if (forklift.isActive())
              forklift.addExceptionalEvent(
                  "Attenzione! Il muletto "
                      + fork.getForklift().getId()
                      + " e' stato coinvolto in una collisione!");
            // TODO: EVENTO ECCEZIONALE DI COLLISIONE AVVENUTA (TRA UNITà IN GUIDA MANUALE E
            // AUTOMATICA)
            // fork.getForklift().write("STOP,"+0+";");
          } else if (fork.isCriticalRecalculating()) {
            System.out.println("Dealing with a critical recalculation");
            String nextPath = fork.getForklift().getPathToNextTaskWithRandomMidpoint();
            fork.getForklift().write("PATH," + nextPath + ";");
          } else if (fork.isRecalculating()) {
            String nextPath = forklift.getPathToNextTaskWithObstacles(fork.getObstacles());
            String currentPath = forklift.getCurrentPathString();
            /*if(nextPath.equals(currentPath)) {
              System.out.println("*****PERCORSO UGUALE AL PRECEDENTE*****");
              fork.getForklift().addMove(Move.STOP);
            }
            else {*/
            forklift.write("PATH," + nextPath + ";");
            /*}*/

          } else if (fork.isInStop()) {
            int stops = fork.getStops();
            if (forklift.isActive()) System.out.println("SENDING STOP..." + stops);
            forklift.write("STOP," + stops + ";");
            for (int i = 0; i < stops; ++i) {
              if (forklift.isActive()) forklift.addMove(Move.STOP);
            }
          }
        });

    forkliftsList.goWithNextMove();

    StringBuilder b = new StringBuilder();
    if (!exceptionalEvents.isEmpty()) {
      exceptionalEvents.stream()
          .forEach(
              m -> {
                b.append(m);
              });
      exceptionalEvents.clear();
    }
    final String msgEcc = b.isEmpty() ? null : b.toString();
    // USERS UPDATE ON FORKS POSITIONS
    String activeForkliftsPositions = forkliftsList.getActiveForkliftsPositions();
    String activeForkliftsTasks = forkliftsList.getActiveForkliftsTasks();

    usersList.getActiveUsers().stream()
        .parallel()
        .forEach(
            u -> {
              if (msgEcc != null /* && u.getClass()==Admin.class */) { // TODO ECC solo per Admin?
                u.write(msgEcc);
              }
              u.write(activeForkliftsTasks);
              u.write(activeForkliftsPositions);
              // u.writeAndSend(activeForkliftsPositions);
              for (int i = 0; i < 5; ++i) {
                u.writeAndSend("");
                u.processCommunication();
                try {
                  Thread.sleep(8);
                } catch (InterruptedException e) {
                }
              }
              // u.writeAndSend(activeForkliftsTasks+activeForkliftsPositions);
            });

    // USERS JOBS
    // usersList.getActiveUsers().stream().parallel().forEach(Client::processCommunication);

    // TODO multiple interactions with users to recduce time for operations
    /* usersList.getActiveUsers().stream()
        .parallel()
        .forEach(
            u -> {

              u.write(activeForkliftsTasks);
              u.writeAndSend(activeForkliftsPositions);
            });

    usersList.getActiveUsers().stream().parallel().forEach(Client::processCommunication);

    usersList.getActiveUsers().stream()
        .parallel()
        .forEach(
            u -> {

              u.write(activeForkliftsTasks);
              u.writeAndSend(activeForkliftsPositions);
            });

    usersList.getActiveUsers().stream().parallel().forEach(Client::processCommunication);  */
  }
}
