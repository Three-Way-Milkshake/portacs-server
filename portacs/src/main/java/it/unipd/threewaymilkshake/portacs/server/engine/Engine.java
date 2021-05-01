/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.Client;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
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

  @Scheduled(fixedDelay = 1000, initialDelay = 3000)
  public void execute() {
    // System.out.println("Hello "+(counter++));
    forkliftsList.getActiveForklifts().stream().parallel().forEach(Client::processCommunication);

    // TODO: execute Collision Pipeline

    usersList.getActiveUsers().stream().parallel().forEach(Client::processCommunication);

    usersList.getActiveUsers().stream()
        .parallel()
        .forEach(
            u -> {
              u.writeAndSend(forkliftsList.getForkliftsPositions());
            });

      // costruzione pipeline: dove va fatta? //TODO
      // CollisionPipeline<ForkliftsList,Map<String, Action>> collisionPipeline = new CollisionPipeline<>(new CollisionDetector())
      // .addHandler(new CollisionSolver());
      // avvio -> //TODO
      // collisionPipeline.execute(forkliftsList)
  }
}
