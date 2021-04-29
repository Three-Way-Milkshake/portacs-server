/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.persistency.UserDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

public class UsersList {
  private Map<String, User> usersMap;
  // encode(raw): String   |   matches(raw, encoded): boolean
  private PasswordEncoder pwdEncoder;
  private UserDao userDao;

  private static final String UNRECOGNIZED_USER = "FAILED; Unrecognized user";
  private static final String WRONG_PWD = "FAILED; Wrong password";

  public UsersList(UserDao userDao, PasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.pwdEncoder = passwordEncoder;
    usersMap = new HashMap<>();
    userDao.readUsers().stream()
        .forEach(
            u -> {
              usersMap.put(u.getId(), u);
            });
  }

  public boolean auth(Connection c) {
    boolean success = false;

    String id = c.read();
    String rawPwd = c.read();
    User u = usersMap.get(id);
    if (u != null) { // user exists
      if (u.authenticate(rawPwd)) {
        success = true;
        u.bindConnection(c);
      } else {
        c.send(WRONG_PWD);
        c.close();
      }
    } else {
      c.send(UNRECOGNIZED_USER);
      c.close();
    }

    return success;
  }

  public List<User> getActiveUsers(){
    return usersMap.values().stream()
      .filter(u->u.isActive())
      .collect(Collectors.toList());
  }
}
