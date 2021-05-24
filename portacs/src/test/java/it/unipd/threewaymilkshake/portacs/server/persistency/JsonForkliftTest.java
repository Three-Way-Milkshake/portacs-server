/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.*;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
public class JsonForkliftTest {

  @Autowired
  @Qualifier("jsonForkliftTest")
  private JsonForklift jsonForklift;

  private List<Forklift> forkliftsList;

  @BeforeEach
  public void setUp() {
    forkliftsList = new LinkedList<Forklift>();
    Forklift forklift = new Forklift("first.forklift", "token.forklift");
    forkliftsList.add(forklift);
  }

  @Test
  @DisplayName("Tests if updating the set of forklifts in the JSON persistency works")
  public void updateForkliftTest() throws JSONException {
    jsonForklift.updateForklifts(forkliftsList);

    Scanner created;
    Scanner compare;
    try {
      created = new Scanner(new File(jsonForklift.getFilePath()), "UTF-8");
      String createdContent = created.useDelimiter("\\Z").next();
      compare =
          new Scanner(
              new File(
                  "src/test/java/it/unipd/threewaymilkshake/portacs/server/database/forkliftComparisonTest.json"),
              "UTF-8");
      String comparedContent = compare.useDelimiter("\\Z").next();
      // assertEquals(createdContent,comparedContent);
      JSONAssert.assertEquals(createdContent, comparedContent, false);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  @Disabled
  @DisplayName("Tests if the reading of forklifts in the JSON persistency works")
  public void readForkliftTest() {
    List<Forklift> readForklifts = jsonForklift.readForklifts();
    IntStream.range(0, forkliftsList.size())
        .forEach(
            i -> {
              assertTrue(new ReflectionEquals(forkliftsList.get(i)).matches(readForklifts.get(i)));
            });
  }
}
