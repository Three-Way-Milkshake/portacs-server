/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import com.google.gson.annotations.Expose;
import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequencesList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.stereotype.Component;

@Component
public abstract class Client implements PropertyChangeListener {
  @Expose protected String id;
  protected boolean active = false;
  protected Connection connection = null;

  // @Autowired
  protected WarehouseMap warehouseMap;
  protected TasksSequencesList tasksSequencesList;

  protected Client(String id) {
    this.id = id;
  }

  public boolean isActive() {
    return active;
  }

  public String getId() {
    return id;
  }

  void setWarehouseMap(WarehouseMap warehouseMap) {
    this.warehouseMap = warehouseMap;
  }

  void setTasksSequencesList(TasksSequencesList tasksSequencesList) {
    this.tasksSequencesList = tasksSequencesList;
  }

  void bindConnection(Connection c) {
    connection = c;
    active = true;
    warehouseMap.addPropertyChangeListener(this);
  }

  void clearConnection() {
    if (connection != null) connection.close();
    connection = null;
    active = false;
    warehouseMap.removePropertyChangeListener(this);
  }

  public void propertyChange(PropertyChangeEvent e) {
    // connection.writeToBuffer(e.getNewValue().toString());
    if (active) {
      writeMap();
      System.out.println("*** MAP IS (from client): " + warehouseMap.toString());
      // connection.writeToBuffer(e.getNewValue().toString());
      writePois();
    }
  }

  void writeMap() {
    connection.writeToBuffer(warehouseMap.toString());
  }

  void writePois() {
    connection.writeToBuffer(warehouseMap.poisToString());
  }

  /**
   * Adds a message to the buffer but does not send it
   *
   * @param s: the message to write
   * @return true if writing to buffer went good
   */
  public boolean write(String s) {
    return connection != null ? connection.writeToBuffer(s) : false;
  }

  /**
   * Adds a message to the buffer and sends it
   *
   * @param s: the message to send
   * @return true if writing to buffer went good
   */
  public boolean writeAndSend(String s) {
    return connection != null ? connection.send(s) : false;
  }

  public boolean send() {
    return connection.sendBuffer();
  }

  public abstract void processCommunication();

  public abstract boolean authenticate(String s);
}
