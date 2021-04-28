/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;
import java.util.List;

public interface UserDao {
  void updateUsers(List<User> u);

  List<User> readUsers();
}
