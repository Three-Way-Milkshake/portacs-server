/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.Client;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.Action;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionDetector;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionPipeline;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionSolver;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

import java.util.Map;
import java.util.Deque;
import java.util.List;

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
  CollisionPipeline<ForkliftsList,Map<String, Action>> collisionPipeline;

  // private CollisionDetector collisionDetector=new CollisionDetector();
  // private CollisionSolver collisionSolver=new CollisionSolver();

  /* CollisionPipeline<ForkliftsList,Map<String, Action>> collisionPipeline 
        = new CollisionPipeline<>(new CollisionDetector().setWarehouseMap(warehouseMap))
        .addHandler(new CollisionSolver().setForkliftsList(forkliftsList)); */

  /*CollisionPipeline<ForkliftsList, Map<String, Action>> collisionPipeline = 
    new CollisionPipeline<>(new CollisionDetector()
    .addHandler(new CollisionSolver()));*/

  @Scheduled(fixedDelay = 500, initialDelay = 3000)
  public void execute() {
    System.out.println("Hello from engine "+(counter++)/* +" with map"+warehouseMap */);
    System.out.println("there are "+forkliftsList.getActiveForklifts().size()+
      " forklifts and "+usersList.getActiveUsers().size()+" users active");
    
    // FORKLIFT JOBS
    forkliftsList.getActiveForklifts().stream().parallel().forEach(Client::processCommunication);

    // TODO: execute Collision Pipeline
    /* collisionPipeline.execute(forkliftsList).forEach((fork,actions)->{
      if(!actions.isEmpty()){
        Forklift forklift=forkliftsList.getForklift(fork);
        if(actions.needRecalculation()){
          forklift.write("PATH," + forklift.getPathToNextTask(actions.getObstacle())+ ";");
        }
        else{
          int stops=actions.stopCount();
          forklift.write("STOP,"+stops+";");
        }
      }
    }); */

    
    StringBuilder b=new StringBuilder();
    if(!exceptionalEvents.isEmpty()){
      exceptionalEvents.stream()
        .forEach(m->{
          b.append(m);
        });
      }
    final String msgEcc=b.isEmpty()?null:b.toString();
    //USERS UPDATE ON FORKS POSITIONS
    usersList.getActiveUsers().stream()
        .parallel()
        .forEach(
            u -> {
              if(msgEcc!=null){
                u.write(msgEcc);
              }
              u.writeAndSend(forkliftsList.getForkliftsPositions());
            });

    //USERS JOBS
    usersList.getActiveUsers().stream().parallel().forEach(Client::processCommunication);
  }
}
