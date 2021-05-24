/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequencesList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.UserDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsersList {
  private Map<String, User> usersMap;
  // encode(raw): String   |   matches(raw, encoded): boolean
  // @Autowired
  private PasswordEncoder passwordEncoder;
  private UserDao userDao;

  // @Autowired
  // @Qualifier("warehouseMap")
  private WarehouseMap warehouseMap;
  // @Autowired private PasswordEncoder passwordEncoder;

  private static final String UNRECOGNIZED_USER = "FAIL,Utente non riconosciuto;";
  private static final String WRONG_PWD = "FAIL,Password errata;";
  private static final String ACTIVE_USER = "FAIL,L'utente è al momento attivo;";

  private static final int BASE_PWD_LENGTH = 8;

  // not used ?
  public UsersList(@Qualifier("jsonUser") UserDao userDao, PasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
    System.out.println("map is" + warehouseMap);
    usersMap = new HashMap<>();
    userDao.readUsers().stream()
        .forEach(
            u -> {
              u.setPasswordEncoder(passwordEncoder);
              u.setWarehouseMap(warehouseMap);
              // u.setTasksSequencesList(tasksSequencesList);
              usersMap.put(u.getId(), u);
            });
  }

  @Autowired // the one that spring uses
  public UsersList(
      @Qualifier("jsonUser") UserDao userDao,
      PasswordEncoder passwordEncoder,
      WarehouseMap warehouseMap,
      @Qualifier("tasksSequencesListTest") TasksSequencesList tasksSequencesList,
      ForkliftsList forkliftsList) {
    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
    this.warehouseMap = warehouseMap;
    System.out.println("map is" + warehouseMap);
    usersMap = new HashMap<>();
    userDao.readUsers().stream()
        .forEach(
            u -> {
              u.setPasswordEncoder(passwordEncoder);
              u.setWarehouseMap(warehouseMap);
              u.setTasksSequencesList(tasksSequencesList);
              if (u.getRole().equals("ADMIN")) {
                ((Admin) u).setUsersList(this);
                ((Admin) u).setForkliftsList(forkliftsList);
              }
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
        // u.writeAndSend("OK,MANAGER,a.b,AAAA,BBBBB;");
        u.write("OK," + u.getRole() + ',' + u.getFirstName() + ',' + u.getLastName() + ';');
        // u.write(u.getFirstName()+',');
        // u.write(u.getLastName()+';');
        u.write(warehouseMap.toString());
        u.writeAndSend(warehouseMap.poisToString());
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

  public List<User> getActiveUsers() {
    return usersMap.values().stream().filter(u -> u.isActive()).collect(Collectors.toList());
  }

  String addUser(String type, String firstName, String lastName) {
    String newUserId = firstName.toLowerCase() + "." + lastName.toLowerCase();
    if (usersMap.containsKey(newUserId)) {
      newUserId = getAvailableId(newUserId);
    }
    String pwd = generateRandomPwd();
    String pwdHash = passwordEncoder.encode(pwd);
    User newUser =
        switch (type) {
          case "MANAGER" -> new Manager(newUserId, firstName, lastName, pwdHash);
          case "ADMIN" -> new Admin(newUserId, firstName, lastName, pwdHash);
          default -> null;
        };
    newUser.setPasswordEncoder(passwordEncoder);
    newUser.setWarehouseMap(warehouseMap);
    usersMap.put(newUserId, newUser);

    userDao.updateUsers(usersMap.values().stream().collect(Collectors.toList()));

    return "ADU," + newUserId + ',' + pwd + ';';
  }

  String removeUser(String userId) {
    String res = null;
    if (!usersMap.containsKey(userId)) {
      res = "RMU," + UNRECOGNIZED_USER;
    } else if (usersMap.get(userId).isActive()) {
      res = "RMU," + ACTIVE_USER;
    } else {
      usersMap.remove(userId);
      res = "RMU,OK";
    }

    userDao.updateUsers(usersMap.values().stream().collect(Collectors.toList()));

    return res + ';';
  }

  String editUserFirstName(String userId, String newFirstName) {
    String res = "EDU,";
    if (!usersMap.containsKey(userId)) {
      res += UNRECOGNIZED_USER;
    } else {
      usersMap.get(userId).setFirstName(newFirstName);
      res += "OK";
    }

    userDao.updateUsers(usersMap.values().stream().collect(Collectors.toList()));

    return res + ';';
  }

  String editUserLastName(String userId, String newLastName) {
    String res = "EDU,";
    if (!usersMap.containsKey(userId)) {
      res += UNRECOGNIZED_USER;
    } else {
      usersMap.get(userId).setLastName(newLastName);
      res += "OK";
    }

    userDao.updateUsers(usersMap.values().stream().collect(Collectors.toList()));

    return res + ';';
  }

  String resetUserPassword(String userId) {
    String res = "EDU,";
    if (!usersMap.containsKey(userId)) {
      res += UNRECOGNIZED_USER;
    } else {
      String newPwd = generateRandomPwd();
      String pwdHash = passwordEncoder.encode(newPwd);
      usersMap.get(userId).setPwdHash(pwdHash);
      res += "OK," + newPwd;
    }

    userDao.updateUsers(usersMap.values().stream().collect(Collectors.toList()));

    return res + ';';
  }

  /** @return forklifts list as Three Way protocolo (LISTU,N,ID1,UN1,UL1,R1...) */
  public String getUsersDetailsList() {
    StringBuilder b = new StringBuilder();
    b.append("LISTU,");
    b.append(usersMap.size());
    b.append(',');
    usersMap.forEach(
        (k, v) -> {
          b.append(k);
          b.append(',');
          b.append(v.getFirstName());
          b.append(',');
          b.append(v.getLastName());
          b.append(',');
          b.append(v.getRole());
          b.append(',');
        });

    b.deleteCharAt(b.length() - 1);
    b.append(';');

    return b.toString();
  }

  private String generateRandomPwd() {
    /* return new Random(System.currentTimeMillis())
    .ints()
    .limit(BASE_PWD_LENGTH)
    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
    .toString(); */
    // return "PASSWORDFAKE";
    return new RandomString(BASE_PWD_LENGTH).nextString();
  }

  /**
   * @param base the starting id
   * @return the id of the new user considering all omonyms
   */
  private String getAvailableId(String base) {
    int c = 1;
    base = base + '.';
    while (usersMap.containsKey(base + c)) {
      ++c;
    }
    return base + c;
  }
}
