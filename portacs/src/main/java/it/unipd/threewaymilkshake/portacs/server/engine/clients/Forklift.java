package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.util.Arrays;
import java.util.List;

import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequence;

public class Forklift extends Client{
  private String token;
  private TasksSequence tasks;
  private List<Move> pathToNextTask;
  private Position position;

  Forklift(String id, String token){
    super(id);
    this.token=token;
  }

  @Override
  public void processCommunication() {
    if(connection.isAlive()){
      String[] commands=connection.getLastMessage().split(";");
      Arrays.stream(commands)
        .forEach(c->{
          String[] par=c.split(",");
          System.out.print("Command: "+par[0]+", params: ");
          for(int i=1; i<par.length; ++i){
            System.out.print(par[i]+" ");
          }
          System.out.println();
          switch(par[0]){
            case "POS": 
              updatePosition(par); 
              System.out.println("I am at: "+position.toString());
              break;
            case "PATH": 
              if(par[1].equals("1")) tasks.extractNext();
              connection.writeToBuffer("PATH,"+getPathToNextTask()+";"); 
              break;
            case "MAP": break;
            default: 
              System.out.println("Unrecognized message: "+par[0]);
          }
      });
    }
    else{
      clearConnection();
    }
  }

  private String getPathToNextTask(){
    pathToNextTask=map.getPath(position, tasks.getNext());
    return pathToNextTask.toString().replaceAll("\\[|\\]", "");
  }

  private void updatePosition(String... pos){
    position.setPosition(
      Integer.parseInt(pos[1]),
      Integer.parseInt(pos[2]),
      Orientation.values()[Integer.parseInt(pos[3])]
    );
  }

  @Override
  public boolean authenticate(String s) {
    return s.equals(token);
  }
}