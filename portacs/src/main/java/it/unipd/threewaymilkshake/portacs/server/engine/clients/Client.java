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

  /**
   * Adds a message to the buffer but does not send it
   * @param s: the message to write
   * @return true if writing to buffer went good
   */
  public boolean write(String s){
    return connection.writeToBuffer(s);
  }

  /**
   * Adds a message to the buffer and sends it
   * @param s: the message to send
   * @return true if writing to buffer went good
   */
  public boolean writeAndSend(String s){
    return connection.send(s);
  }

  public boolean send(){
    return connection.sendBuffer();
  }

  public abstract void processCommunication();

  public abstract boolean authenticate(String s);
}
