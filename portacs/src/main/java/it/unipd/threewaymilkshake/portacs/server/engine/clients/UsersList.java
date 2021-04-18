package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.persistency.UserDao;

public class UsersList{
  private Map<String, User> usersMap;
  //encode(raw): String   |   matches(raw, encoded): boolean
  private PasswordEncoder pwdEncoder;
  private UserDao userDao;

  private final static String UNRECOGNIZED_USER="FAILED; Unrecognized user";
  private final static String WRONG_PWD="FAILED; Wrong password";

  public UsersList(UserDao userDao, PasswordEncoder passwordEncoder){
    this.userDao=userDao;
    this.pwdEncoder=passwordEncoder;
    usersMap=new HashMap<>();
    userDao.readUsers().stream().forEach(u->{
      usersMap.put(u.getId(), u);
    });
  }

  public boolean auth(Connection c){
    boolean success=false;
    
    String id=c.read();
    String rawPwd=c.read();
    User u=usersMap.get(id);
    if(u!=null){ //user exists
      
    }
    else{
      c.send(UNRECOGNIZED_USER);
      c.close();
    }

    return success;
  }
}