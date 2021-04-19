package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

abstract class Client implements PropertyChangeListener{
  protected String id;
  protected boolean active=false;
  protected Connection connection=null;
  @Autowired WarehouseMap map;

  protected Client(String id){
    this.id=id;
  }

  public boolean isActive(){
    return active;
  }

  public String getId(){
    return id;
  }

  void bindConnection(Connection c){
    connection=c;
    active=true;
  }

  void clearConnection(){
    connection.close();
    connection=null;
    active=false;
  }

  public void propertyChange(PropertyChangeEvent e){
    throw new RuntimeException();
    // connection.writeToBuffer(map.toString());
  }

  public abstract void processCommunication();
  public abstract boolean authenticate(String s);
}