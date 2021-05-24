/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import java.util.Arrays;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Admin extends User {

  private UsersList usersList;
  private ForkliftsList forkliftsList;

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

  void setUsersList(UsersList usersList) {
    this.usersList = usersList;
  }

  void setForkliftsList(ForkliftsList forkliftsList) {
    this.forkliftsList = forkliftsList;
  }

  private void editMap(int rows, int cols, /* List<Integer> */ String seq) {
    int counter = 0;
    CellType[][] mapStructure = new CellType[rows][cols];
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        mapStructure[i][j] = CellType.values()[seq.charAt(counter++) - '0'];
      }
    }
    warehouseMap.setMap(mapStructure);
    connection.writeToBuffer("MAP,OK");
  }

  private void editCell(int x, int y, String... actions) {
    warehouseMap.setCell(x, y, actions);
    connection.writeToBuffer("CELL,OK");
  }

  private boolean isMapEditable() {
    return forkliftsList.getActiveForklifts().size() == 0;
  }

  private void addUser(String type, String firstName, String lastName) {
    String msg = usersList.addUser(type, firstName, lastName);
    connection.writeToBuffer(msg);
  }

  private void removeUser(String userToRemoveId) {
    String msg = usersList.removeUser(userToRemoveId);
    connection.writeToBuffer(msg);
  }

  private void editUser(String userToEditId, String... actions) {
    // EDU,ID,A,PAR
    // actions=[A,PAR] (PAR assente in caso di reset password)
    String msg =
        switch (actions[0]) {
          case "NAME" -> usersList.editUserFirstName(userToEditId, actions[1]);
          case "LAST" -> usersList.editUserLastName(userToEditId, actions[1]);
          case "RESET" -> usersList.resetUserPassword(userToEditId);
          default -> null;
        };
    connection.writeToBuffer(msg);
  }

  private void addForklift(String newForkliftId) {
    String msg = forkliftsList.addForklift(newForkliftId);
    connection.writeToBuffer(msg);
  }

  private void removeForklift(String forkliftToRemoveId) {
    String msg = forkliftsList.removeForklift(forkliftToRemoveId);
    connection.writeToBuffer(msg);
  }

  private void sendForkliftsList() {
    connection.writeToBuffer(forkliftsList.getForkliftsAndTokensString());
  }

  private void sendUsersList() {
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

                /* System.out.print("(admin) " + id + ") Command: " + par[0] + ", params: ");
                for (int i = 1; i < par.length; ++i) {
                  System.out.print(par[i] + " ");
                }
                System.out.println(); */

                switch (par[0]) {
                  case "MAP":
                    if (isMapEditable()) {
                      editMap(
                          Integer.valueOf(par[1]), Integer.valueOf(par[2]), par[3]
                          /* Arrays.stream(par)
                          .skip(3) //TODO check, should be 3
                          .map(Integer::parseInt)
                          .collect(Collectors.toList()) */
                          );
                    } else {
                      connection.writeToBuffer(
                          "MAP,FAIL,La mappa si può modificare solo se nessun muletto è attivo!;");
                      writeMap();
                      writePois();
                    }
                    break;

                  case "CELL":
                    if (isMapEditable()) {
                      editCell(
                          Integer.valueOf(par[1]),
                          Integer.valueOf(par[2]),
                          Arrays.stream(par)
                              .skip(3) // TODO check, should be 3
                              .toArray(String[]::new)
                          // .collect(Collectors.toList())
                          // .toArray()
                          );
                    } else {
                      connection.writeToBuffer(
                          "MAP,FAIL,La mappa si può modificare solo se nessun muletto è attivo!;");
                      // writeMap();
                      // writePois();
                    }
                    break;

                  case "ADU":
                    addUser(par[1], par[2], par[3]);
                    break;

                  case "RMU":
                    removeUser(par[1]);
                    break;

                  case "EDU":
                    editUser(
                        par[1], Arrays.stream(par).skip(2).toArray(String[]::new)
                        // .collect(Collectors.toList())
                        // .toArray()
                        );
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
