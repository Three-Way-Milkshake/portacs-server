/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import it.unipd.threewaymilkshake.portacs.server.engine.map.Poi;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
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
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
// @EnableAutoConfiguration
public class JsonMapTest {

  @Autowired
  @Qualifier("jsonMapTest")
  private JsonMap jsonMap;

  private WarehouseMap warehouseMap;

  @BeforeEach
  public void setUp() {

    CellType[][] templateMapItem = {
      {CellType.OBSTACLE, CellType.NEUTRAL, CellType.UP, CellType.RIGHT},
      {CellType.DOWN, CellType.LEFT, CellType.POI, CellType.POI},
      {CellType.POI, CellType.OBSTACLE, CellType.LEFT, CellType.LEFT},
    };

    List<Poi> pois = new LinkedList<>();
    Poi firstPoi = new Poi(1L, "firstPoi", new SimplePoint(1, 2));
    Poi secondPoi = new Poi(2L, "secondPoi", new SimplePoint(1, 2));
    Poi thirdPoi = new Poi(3L, "thirdPoi", new SimplePoint(2, 1));
    pois.add(firstPoi);
    pois.add(secondPoi);
    pois.add(thirdPoi);

    warehouseMap = new WarehouseMap(templateMapItem, pois, null);
  }

  @Test
  @DisplayName("Tests if updating wharehouse map in the JSON persistency works")
  public void updateMapTest() throws JSONException {
    // assertEquals(jsonMap.getFilePath(),"src/main/java/it/unipd/threewaymilkshake/portacs/server/database/mapTest.json");
    jsonMap.updateMap(warehouseMap);

    Scanner created;
    Scanner compare;
    try {
      created = new Scanner(new File(jsonMap.getFilePath()));
      String createdContent = created.useDelimiter("\\Z").next();
      compare =
          new Scanner(
              new File(
                  "src/test/java/it/unipd/threewaymilkshake/portacs/server/database/mapComparisonTest.json"));
      String comparedContent = compare.useDelimiter("\\Z").next();
      JSONAssert.assertEquals(createdContent, comparedContent, true);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("Tests if reading the warehouse map in the JSON persistency works")
  public void readMapTest() {
    WarehouseMap readMap = jsonMap.readMap();
    assertTrue(new ReflectionEquals(warehouseMap.getMap()).matches(readMap.getMap()));
    assertEquals(warehouseMap.getPois(), readMap.getPois());
  }
}
