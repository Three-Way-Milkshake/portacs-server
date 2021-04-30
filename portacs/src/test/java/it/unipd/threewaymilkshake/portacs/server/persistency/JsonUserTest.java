/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Admin;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Manager;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.*;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
// @EnableAutoConfiguration
public class JsonUserTest {

  @Autowired
  @Qualifier("jsonUserTest")
  private JsonUser jsonUser;

  private List<User> usersList;

  @BeforeEach
  public void setUp() {
    usersList = new LinkedList<User>();
    User user1 = new Admin("first.user", "First", "User", "abcd");
    User user2 = new Manager("second.user", "Second", "User", "efgh");
    usersList.add(user1);
    usersList.add(user2);
  }

  @Test
  @DisplayName("Tests if updating the set of users in the JSON persistency works")
  public void updateUserTest2() throws JSONException {
    jsonUser.updateUsers(usersList);

    Scanner created;
    Scanner compare;
    try {
      created = new Scanner(new File(jsonUser.getFilePath()), "UTF-8");
      String createdContent = created.useDelimiter("\\Z").next();
      compare =
          new Scanner(
              new File(
                  "src/test/java/it/unipd/threewaymilkshake/portacs/server/database/userComparisonTest.json"),
              "UTF-8");
      String comparedContent = compare.useDelimiter("\\Z").next();
      JSONAssert.assertEquals(createdContent, comparedContent, false);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("Tests if the reading of users in the JSON persistency works")
  public void readUserTest() {
    List<User> readUsers = jsonUser.readUsers();
    IntStream.range(0, usersList.size())
        .forEach(
            i -> {
              assertTrue(new ReflectionEquals(usersList.get(i)).matches(readUsers.get(i)));
            });
  }
}
