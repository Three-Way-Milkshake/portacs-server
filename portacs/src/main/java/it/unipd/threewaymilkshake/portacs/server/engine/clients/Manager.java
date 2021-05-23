/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Manager extends User {

  // @Autowired private TasksSequencesList tasksSequenczesList;

  public Manager(String id, String firstName, String lastName, String pwdHash) {
    super(id, firstName, lastName, pwdHash);
  }

  Manager(
      String id,
      String firstName,
      String lastName,
      String pwdHash,
      PasswordEncoder passwordEncoder) {
    super(id, firstName, lastName, pwdHash, passwordEncoder);
  }

  private void addTasksSequence(List<Long> tasks) {
    long newListId = tasksSequencesList.addTasksSequence(new LinkedBlockingDeque<>(tasks));
    StringBuilder b = new StringBuilder();
    b.append("ADL,OK,");
    b.append(newListId);
    b.append(';');
    connection.writeToBuffer(b.toString());
  }

  private void removeTasksSequence(long listId) {
    if (tasksSequencesList.removeTasksSequence(listId)) {
      connection.writeToBuffer("RML,OK");
    } else {
      connection.writeToBuffer("RML,FAIL,Lista già assegnata");
    }
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

                /* System.out.print("(manager) " + id + ") Command: " + par[0] + ", params: ");
                for (int i = 1; i < par.length; ++i) {
                  System.out.print(par[i] + " ");
                }
                System.out.println(); */

                switch (par[0]) {
                  case "ADL":
                    /* addTasksSequence(
                    Arrays.stream(Arrays.copyOfRange(par, 1, par.length))
                        .map(Long::parseLong)
                        .collect(Collectors.toList())); */
                    addTasksSequence(
                        Arrays.stream(par)
                            .skip(1)
                            .map(Long::parseLong)
                            .collect(Collectors.toList()));
                    break;

                  case "RML":
                    removeTasksSequence(Long.valueOf(par[1]));
                    break;

                  default:
                    // already handled by super
                }
              });
    }
  }

  @Override
  String getRole() {
    return "MANAGER";
  }
}
