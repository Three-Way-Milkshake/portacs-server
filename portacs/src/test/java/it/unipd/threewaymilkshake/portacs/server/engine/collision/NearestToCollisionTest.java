/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class NearestToCollisionTest {

  @Test
  @DisplayName("Tests if the stops are assigned to the right unit: no unit is already in stop")
  public void nearestCanGoTest() {
    Forklift f1 = new Forklift("1", "asfdsfd");
    f1.setPosition(new Position(1, 2, Orientation.DOWN));
    Forklift f2 = new Forklift("2", "asfdsfd");
    f2.setPosition(new Position(2, 4, Orientation.LEFT));

    CollisionForklift first = new CollisionForklift(f1);
    CollisionForklift second = new CollisionForklift(f2);

    Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
    collisions.put(new SimplePoint(2, 2), new LinkedList<>(List.of(first, second)));

    NearestToCollision nearestToCollision = new NearestToCollision();
    nearestToCollision.process(collisions);

    assertEquals(1, second.getStops());
  }

  @Test
  @DisplayName(
      "Tests if the stops are assigned to the right unit: the farthest unit is already in stop")
  public void nearestCanGoButFarthesIsAlreadyInStopTest() {
    Forklift f1 = new Forklift("1", "asfdsfd");
    f1.setPosition(new Position(1, 2, Orientation.DOWN));
    Forklift f2 = new Forklift("2", "asfdsfd");
    f2.setPosition(new Position(2, 4, Orientation.LEFT));

    CollisionForklift first = new CollisionForklift(f1);
    CollisionForklift second = new CollisionForklift(f2);
    second.addStop();

    Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
    collisions.put(new SimplePoint(2, 2), new LinkedList<>(List.of(first, second)));

    NearestToCollision nearestToCollision = new NearestToCollision();
    nearestToCollision.process(collisions);

    assertEquals(1, second.getStops());
  }

  @Test
  @DisplayName(
      "Tests if the stops are assigned to the right unit: the farthest unit is already recalculating")
  public void nearestCanGoButFarthesIsAlreadyRecalculatingTest() {
    Forklift f1 = new Forklift("1", "asfdsfd");
    f1.setPosition(new Position(1, 2, Orientation.DOWN));
    Forklift f2 = new Forklift("2", "asfdsfd");
    f2.setPosition(new Position(2, 4, Orientation.LEFT));

    CollisionForklift first = new CollisionForklift(f1);
    CollisionForklift second = new CollisionForklift(f2);
    second.setRecalculate(new Position(2, 3, Orientation.UP));

    Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
    collisions.put(new SimplePoint(2, 2), new LinkedList<>(List.of(first, second)));

    NearestToCollision nearestToCollision = new NearestToCollision();
    nearestToCollision.process(collisions);

    assertEquals(0, second.getStops());
  }

  @Test
  @DisplayName(
      "Tests if the stops are assigned to the right unit: the units are equally distant from the collision point")
  public void unitsEquallyDistantFromCollisionPointTest() {
    Forklift f1 = new Forklift("1", "asfdsfd");
    f1.setPosition(new Position(0, 2, Orientation.DOWN));
    Forklift f2 = new Forklift("2", "asfdsfd");
    f2.setPosition(new Position(2, 4, Orientation.LEFT));

    CollisionForklift first = new CollisionForklift(f1);
    CollisionForklift second = new CollisionForklift(f2);

    Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
    collisions.put(new SimplePoint(2, 2), new LinkedList<>(List.of(first, second)));

    NearestToCollision nearestToCollision = new NearestToCollision();
    nearestToCollision.process(collisions);
    assertEquals(true, (second.isInStop() || first.isInStop()));
  }
}
