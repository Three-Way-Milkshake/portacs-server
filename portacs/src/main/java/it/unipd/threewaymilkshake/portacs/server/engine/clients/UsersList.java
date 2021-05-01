/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.Pair;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.UserDao;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsersList {
  private Map<String, User> usersMap;
  // encode(raw): String   |   matches(raw, encoded): boolean
  private PasswordEncoder pwdEncoder;
  private UserDao userDao;

  @Autowired private WarehouseMap warehouseMap;
  @Autowired private PasswordEncoder passwordEncoder;

  private static final String UNRECOGNIZED_USER = "FAIL,Unrecognized user";
  private static final String WRONG_PWD = "FAIL,Wrong password";
  private static final String ACTIVE_USER="FAIL,User is active";

  private static final int BASE_PWD_LENGTH=8;

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
        u.write("OK,"+u.getRole()+";");
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

  AbstractMap.SimpleEntry<String,String> addUser(String type, String firstName, String lastName){
    String newUserId=firstName+"."+lastName;
    if(usersMap.containsKey(newUserId)){
      newUserId=getAvailableId(newUserId);
    }
    String pwd=generateRandomPwd();
    String pwdHash=passwordEncoder.encode(pwd);
    User newUser=switch(type){
      case "MANAGER" -> new Manager(newUserId, firstName, lastName, pwdHash);
      case "ADMIN" -> new Admin(newUserId, firstName, lastName, pwdHash);
      default -> null;
    };
    usersMap.put(newUserId, newUser);

    return new AbstractMap.SimpleEntry<>(newUserId, pwd);
  }

  String removeUser(String userId){
    String r=null;
    if(!usersMap.containsKey(userId)){
      r="RMU,"+UNRECOGNIZED_USER;
    }
    else if(usersMap.get(userId).isActive()){
      r="RMU,"+ACTIVE_USER;
    }
    else{
      usersMap.remove(userId);
      r="RMU,OK";
    }

    return r;
  }

  /**
   * @return forklifts list as Three Way protocolo
   * (LISTU,N,ID1,UN1,UL1,R1...)
   */
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
          b.append(v.getFirstName());
          b.append(',');
          b.append(v.getRole());
          b.append(',');
        });

    b.deleteCharAt(b.length() - 1);
    b.append(';');

    return b.toString();
  }

  private String generateRandomPwd() {
    return new Random(System.currentTimeMillis())
      .ints()
      .limit(BASE_PWD_LENGTH)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();
  }

  /**
   * @param base the starting id
   * 
   * @return the id of the new user considering all omonyms
   */
  private String getAvailableId(String base){
    int c=1;
    base=base+'.';
    while(usersMap.containsKey(base+c)){
      ++c;
    }
    return base+c;
  }
}
