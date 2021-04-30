/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class JsonUser implements UserDao {

  private String filePath;

  public JsonUser(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public void updateUsers(List<User> u) {
    Type listType = new TypeToken<LinkedList<User>>() {}.getType();
    Gson gson =
        new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(User.class, new JsonUserAdapter())
            .create();

    String serialized = gson.toJson(u, listType);
    try (Writer writer = new FileWriter(filePath)) {
      writer.write(serialized);
    } catch (IOException e) {
      System.out.println("Error during file opening");
      e.printStackTrace();
    }
  }

  @Override
  public List<User> readUsers() {
    Gson gson =
        new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(User.class, new JsonUserAdapter())
            .setPrettyPrinting()
            .create();
    Type listType = new TypeToken<LinkedList<User>>() {}.getType();
    try {
      List<User> deserialized = gson.fromJson(new FileReader(this.filePath), listType);
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
