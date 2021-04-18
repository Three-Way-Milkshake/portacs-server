package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;
import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import it.unipd.threewaymilkshake.portacs.server.engine.map.Poi;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

public class JsonUser implements UserDao {

  private String filePath;

  public JsonUser(String filePath) {
    this.filePath = filePath;
  }

  @Override //TODO: capire perchè usa List<User> e non UserList
  public void updateUsers(List<User> u) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String serialized = gson.toJson(u);
    try (Writer writer = new FileWriter(filePath)) {
      writer.write(serialized);
    } catch (IOException e) {
      System.out.println("Error during file opening");
      e.printStackTrace();
    }

  }

  @Override
  public List<User> readUsers() {
    /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      List<User> deserialized = gson.fromJson(new FileReader(this.filePath), List<User>.class);
      return deserialized;
    } catch (FileNotFoundException e) {
      System.out.println("The file does not exist!");
      // TODO define the behviour
      e.printStackTrace();
    }*/
    return null;
  }

}