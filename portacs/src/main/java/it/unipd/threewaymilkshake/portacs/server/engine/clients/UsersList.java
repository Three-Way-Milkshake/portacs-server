package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.util.HashMap;
import java.util.Map;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;

public class UsersList{
  private Map<String, User> usersMap=new HashMap<>();

  public boolean auth(Connection c){
    boolean success=true;
    //TODO implement
    return success;
  }
}