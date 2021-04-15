package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import org.springframework.beans.factory.annotation.Autowired;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.Observer;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

abstract class Client implements Observer{
  protected String id;
  protected boolean active=false;
  protected Connection connection;
  @Autowired WarehouseMap map;

  Client(String id){
    this.id=id;
  }

  boolean isActive(){
    return active;
  }

  void bindConnection(Connection c){
    connection=c;
    active=true;
  }

  public void update(){
    connection.writeToBuffer(map.toString());
  }
}