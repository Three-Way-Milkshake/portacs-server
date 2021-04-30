/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;

public class Admin extends User {

  public Admin(String id, String firstName, String lastName, String pwdHash) {
    super(id, firstName, lastName, pwdHash);
  }

  public Admin(
      String id,
      String firstName,
      String lastName,
      String pwdHash,
      PasswordEncoder passwordEncoder) {
    super(id, firstName, lastName, pwdHash, passwordEncoder);
  }

  private void editMap(int rows, int cols, List<Integer> seq){
    int counter=0;
    CellType[][] mapStructure=new CellType[rows][cols];
    for(int i=0; i<rows; ++i){
      for(int j=0; j<cols; ++j){
        mapStructure[i][j]=CellType.values()[seq.get(counter++)];
      }
    }
    warehouseMap.setMap(mapStructure);
    connection.writeToBuffer("MAP,OK");
  }

  private void editCell(int x, int y, String... actions){
    CellType type=CellType.values()[Integer.valueOf(actions[0])];
    warehouseMap.setCell(x, y, type);
    if(type==CellType.POI){
      //TODO poi stuff
    }
    //TODO finish
  }

  @Override
  public void processCommunication() {
    super.processCommunication(); 
    if (active) {
      String[] commands = connection.getLastMessage().split(";");
      Arrays.stream(commands)
          .forEach(
              c -> {
                String[] par = c.split(",");

                System.out.print(id + ") Command: " + par[0] + ", params: ");
                for (int i = 1; i < par.length; ++i) {
                  System.out.print(par[i] + " ");
                }
                System.out.println();

                switch (par[0]) {
                  case "MAP":
                    editMap(
                      Integer.valueOf(par[1]), 
                      Integer.valueOf(par[2]), 
                      Arrays.stream(par)
                        .skip(3)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
                    );
                    break;
                  
                  case "CELL":
                    //editCell(); //TODO finish

                  default:
                    // already handled by super
                }
              });
    }
  }

  @Override
  String getRole() {
    return "ADMIN";
  }
}
