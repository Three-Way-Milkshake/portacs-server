/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;

public class Admin extends User {

  @Autowired private UsersList usersList;
  @Autowired private ForkliftsList forkliftsList;

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
    warehouseMap.setCell(x, y, actions);
    connection.writeToBuffer("CELL,OK");
  }

  private void addUser(String type, String firstName, String lastName){
    AbstractMap.SimpleEntry<String,String> data=usersList.addUser(type, firstName, lastName);
    String res="ADU,"+data.getKey()+","+data.getValue()+";";
    connection.writeToBuffer(res);
  }

  private void removeUser(String userToRemoveId){
    String msg=usersList.removeUser(userToRemoveId);
    connection.writeToBuffer(msg);
  }

  private void addForklift(String newForkliftId){
    String msg=forkliftsList.addForklift(newForkliftId);
    connection.writeToBuffer(msg);
  }

  private void removeForklift(String forkliftToRemoveId){
    String msg=forkliftsList.removeForklift(forkliftToRemoveId);
    connection.writeToBuffer(msg);
  }

  private void sendForkliftsList(){
    connection.writeToBuffer(forkliftsList.getForkliftsAndTokensString());
  }

  private void sendUsersList(){
    connection.writeToBuffer(usersList.getUsersDetailsList());
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
                    editCell(
                      Integer.valueOf(par[1]),
                      Integer.valueOf(par[2]),
                      (String[])Arrays.stream(par)
                        .skip(3)
                        .collect(Collectors.toList())
                        .toArray()
                    );
                    break;

                  case "ADU":
                    addUser(par[1], par[2], par[3]);
                    break;

                  case "RMU":
                    removeUser(par[1]);
                    break;

                  case "ADF":
                    addForklift(par[1]);
                    break;

                  case "RMF":
                    removeForklift(par[1]);
                    break;

                  case "LISTF":
                    sendForkliftsList();
                    break;

                  case "LISTU":
                    sendUsersList();
                    break;

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
