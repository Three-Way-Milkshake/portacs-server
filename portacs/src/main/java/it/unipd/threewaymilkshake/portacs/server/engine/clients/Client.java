/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Client implements PropertyChangeListener {
  protected String id;
  protected boolean active = false;
  protected Connection connection = null;
  @Autowired WarehouseMap map;

  protected Client(String id) {
    this.id = id;
  }

  public boolean isActive() {
    return active;
  }

  public String getId() {
    return id;
  }

  void bindConnection(Connection c) {
    connection = c;
    active = true;
  }

  void clearConnection() {
    connection.close();
    connection = null;
    active = false;
  }

  public void propertyChange(PropertyChangeEvent e) {
    connection.writeToBuffer(e.getNewValue().toString());
  }

  public abstract void processCommunication();

  public abstract boolean authenticate(String s);
}
