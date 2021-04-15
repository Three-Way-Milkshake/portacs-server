package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.util.HashMap;
import java.util.Map;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;

public class ForkliftsList{
  private Map<String, User> forkliftsMap=new HashMap<>();

  public boolean auth(Connection c){
    boolean success=true;
    //TODO implement
    return success;
  }
}