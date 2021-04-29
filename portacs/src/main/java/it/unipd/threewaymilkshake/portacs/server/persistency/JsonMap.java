/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class JsonMap implements MapDao {

  private String filePath;

  public JsonMap(String filePath) {
    this.filePath = filePath;
  }

  public String getFilePath() {
    return filePath;
  }

  /** saves the map every time some change is applied */
  @Override
  public void updateMap(WarehouseMap m) {
    Gson gson =
        new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    String serialized = gson.toJson(m);
    try (Writer writer = new FileWriter(filePath)) {
      writer.write(serialized);
    } catch (IOException e) {
      System.out.println("Error during file opening");
      e.printStackTrace();
    }
  }

  /** parsing of the json file containing the map */
  @Override
  public WarehouseMap readMap() {
    Gson gson =
        new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
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
