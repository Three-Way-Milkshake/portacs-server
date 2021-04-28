/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class JsonUser implements UserDao {

  private String filePath;

  public JsonUser(String filePath) {
    this.filePath = filePath;
  }

  @Override // TODO: there must be a problem here
  public void updateUsers(List<User> u) {

    Gson gson =
        new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

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
    Type listType = new TypeToken<LinkedList<User>>(){}.getType();
    try {
        List<User> deserialized = gson.fromJson(new FileReader(this.filePath), listType);
        return deserialized;
      } catch (FileNotFoundException e) {
        System.out.println("The file does not exist!");
        // TODO define the behviour
        e.printStackTrace();
      }*/
    return null;
  }

  public String getFilePath() {
    return filePath;
  }
}
