/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class JsonForklift implements ForkliftDao {

  private String filePath;

  public JsonForklift(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public void updateForklifts(List<Forklift> f) {
    Gson gson =
        new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    String serialized = gson.toJson(f);
    try (Writer writer = new FileWriter(filePath)) {
      writer.write(serialized);
    } catch (IOException e) {
      System.out.println("Error during file opening");
      e.printStackTrace();
    }
  }

  @Override
  public List<Forklift> readForklifts() {
    Gson gson =
        new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    Type listType = new TypeToken<LinkedList<Forklift>>() {}.getType();
    try {
      List<Forklift> deserialized = gson.fromJson(new FileReader(this.filePath), listType);
      deserialized.forEach(Forklift::initializeFields);
      return deserialized;
    } catch (FileNotFoundException e) {
      System.out.println("The file does not exist!");
      // TODO define the behviour
      e.printStackTrace();
    }
    return null;
  }

  public String getFilePath() {
    return filePath;
  }
}
