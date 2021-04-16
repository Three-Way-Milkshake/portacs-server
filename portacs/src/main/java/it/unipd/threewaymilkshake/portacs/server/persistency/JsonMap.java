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

import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import it.unipd.threewaymilkshake.portacs.server.engine.map.Poi;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

public class JsonMap implements MapDao{

  private String filePath;

  public JsonMap(String filePath){
    this.filePath=filePath;
  }
  
    public String getFilePath() {
    return filePath;
  }

  /**
   * saves the map every time some change is applied
   */
  @Override
  public void updateMap(WarehouseMap m) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String serialized = gson.toJson(m);
    try (Writer writer = new FileWriter(filePath)) {
      writer.write(serialized);
    } catch (IOException e) {
      System.out.println("Error during file opening");
      e.printStackTrace();
    }
  }

  /**
   * parsing of the json file containing the map
   */
  @Override
  public WarehouseMap readMap() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      WarehouseMap deserialized = gson.fromJson(new FileReader(this.filePath), WarehouseMap.class);
      return deserialized;
    } catch (FileNotFoundException e) {
      System.out.println("The file does not exist!");
      // TODO define the behviour
      e.printStackTrace();
    }
    return null;
  }


}