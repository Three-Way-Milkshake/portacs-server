/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import java.util.stream.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class HeadOnCollisionsTest {

  @Mock Forklift forklift;

  @Mock CollisionForklift collisionForklift;

  @Mock Position position;

  @Mock HeadOnCollisions headOnCollisionsMock;

  @Test
  @DisplayName("Tests if the anti head on collision system detects a collision that occurred")
  public void collisionOccurredTest() {
    forklift = mock(Forklift.class);
    collisionForklift = mock(CollisionForklift.class);
    when(collisionForklift.getForklift()).thenReturn(forklift);
    when(forklift.getPosition())
        .thenReturn(new Position(1, 1, Orientation.UP))
        .thenReturn(new Position(1, 1, Orientation.UP));

    HeadOnCollisions headOnCollisions = new HeadOnCollisions();
    headOnCollisions.headOnRisk(collisionForklift, collisionForklift);

    verify(collisionForklift, times(2)).collisionOccurred();
  }

  @Test
  @DisplayName(
      "Tests if the anti head on collision system detects an incoming head on collision between two units which lay on the same x-axis, but y of first unit is less than y of the second")
  public void headOnCollisionWithSameXAxisButFirstYLessThanSecondYTest() {
    forklift = mock(Forklift.class);
    collisionForklift = mock(CollisionForklift.class);
    when(collisionForklift.getForklift()).thenReturn(forklift);
    when(forklift.getPosition())
        .thenReturn(new Position(1, 1, Orientation.RIGHT))
        .thenReturn(new Position(1, 2, Orientation.LEFT));

    HeadOnCollisions headOnCollisions = new HeadOnCollisions();
    boolean returned = headOnCollisions.headOnRisk(collisionForklift, collisionForklift);

    assertEquals(true, returned);
  }

  @Test
  @DisplayName(
      "Tests if the anti head on collision system detects an incoming head on collision between two units which lay on the same x-axis, but y of first unit is more than y of the second")
  public void headOnCollisionWithSameXAxisButFirstYMoreThanSecondYTest() {
    forklift = mock(Forklift.class);
    collisionForklift = mock(CollisionForklift.class);
    when(collisionForklift.getForklift()).thenReturn(forklift);
    when(forklift.getPosition())
        .thenReturn(new Position(1, 2, Orientation.LEFT))
        .thenReturn(new Position(1, 1, Orientation.RIGHT));

    HeadOnCollisions headOnCollisions = new HeadOnCollisions();
    boolean returned = headOnCollisions.headOnRisk(collisionForklift, collisionForklift);

    assertEquals(true, returned);
  }

  @Test
  @DisplayName(
      "Tests if the anti head on collision system detects an incoming head on collision between two units which lay on the same y-axis, but x of first unit is less than x of the second")
  public void headOnCollisionWithSameYAxisButFirstXLessThanSecondXTest() {
    forklift = mock(Forklift.class);
    collisionForklift = mock(CollisionForklift.class);
    when(collisionForklift.getForklift()).thenReturn(forklift);
    when(forklift.getPosition())
        .thenReturn(new Position(1, 2, Orientation.DOWN))
        .thenReturn(new Position(2, 2, Orientation.UP));

    HeadOnCollisions headOnCollisions = new HeadOnCollisions();
    boolean returned = headOnCollisions.headOnRisk(collisionForklift, collisionForklift);

    assertEquals(true, returned);
  }

  @Test
  @DisplayName(
      "Tests if the anti head on collision system detects an incoming head on collision between two units which lay on the same y-axis, but x of first unit is more than x of the second")
  public void headOnCollisionWithSameYAxisButFirstXMoreThanSecondXTest() {
    forklift = mock(Forklift.class);
    collisionForklift = mock(CollisionForklift.class);
    when(collisionForklift.getForklift()).thenReturn(forklift);
    when(forklift.getPosition())
        .thenReturn(new Position(2, 2, Orientation.UP))
        .thenReturn(new Position(1, 2, Orientation.DOWN));

    HeadOnCollisions headOnCollisions = new HeadOnCollisions();
    boolean returned = headOnCollisions.headOnRisk(collisionForklift, collisionForklift);

    assertEquals(true, returned);
  }

  @Test
  @DisplayName("Tests if stop and recalculation is set correctly with first unit already in stop")
  public void headOnCollisionFirstUnitInStopSecondShouldRecalculateTest() {
    forklift = mock(Forklift.class);
    headOnCollisionsMock = mock(HeadOnCollisions.class);
    collisionForklift = mock(CollisionForklift.class);
    when(collisionForklift.getForklift()).thenReturn(forklift);
    when(forklift.getPosition())
        .thenReturn(new Position(2, 2, Orientation.UP))
        .thenReturn(new Position(1, 2, Orientation.DOWN));
    when(collisionForklift.isInStop()).thenReturn(true);

    when(headOnCollisionsMock.headOnRisk(collisionForklift, collisionForklift)).thenReturn(true);
    doCallRealMethod()
        .when(headOnCollisionsMock)
        .setCollisions(collisionForklift, collisionForklift);
    headOnCollisionsMock.setCollisions(collisionForklift, collisionForklift);

    verify(collisionForklift, times(1)).addStop();
    verify(collisionForklift, times(1)).setRecalculate(new Position(2, 2, Orientation.UP));
  }

  @Test
  @DisplayName("Tests if stop and recalculation is set correctly with second unit already in stop")
  public void headOnCollisionSecondUnitInStopFirstShouldRecalculateTest() {
    forklift = mock(Forklift.class);
    headOnCollisionsMock = mock(HeadOnCollisions.class);
    collisionForklift = mock(CollisionForklift.class);
    when(collisionForklift.getForklift()).thenReturn(forklift);
    when(forklift.getPosition())
        .thenReturn(new Position(2, 2, Orientation.UP))
        .thenReturn(new Position(1, 2, Orientation.DOWN));
    when(collisionForklift.isInStop()).thenReturn(false).thenReturn(true);

    when(headOnCollisionsMock.headOnRisk(collisionForklift, collisionForklift)).thenReturn(true);
    doCallRealMethod()
        .when(headOnCollisionsMock)
        .setCollisions(collisionForklift, collisionForklift);
    headOnCollisionsMock.setCollisions(collisionForklift, collisionForklift);

    verify(collisionForklift, times(1)).addStop();
    verify(collisionForklift, times(1)).setRecalculate(new Position(1, 2, Orientation.DOWN));
  }

  @Test
  @DisplayName(
      "Tests if stop and recalculation is set correctly with both or none of the units already in stop")
  public void headOnCollisionRandomlyShouldRecalculateTest() {
    forklift = mock(Forklift.class);
    headOnCollisionsMock = mock(HeadOnCollisions.class);
    collisionForklift = mock(CollisionForklift.class);
    when(collisionForklift.getForklift()).thenReturn(forklift);
    when(forklift.getPosition())
        .thenReturn(new Position(2, 2, Orientation.UP))
        .thenReturn(new Position(1, 2, Orientation.DOWN));
    when(collisionForklift.isInStop()).thenReturn(false).thenReturn(false);

    when(headOnCollisionsMock.headOnRisk(collisionForklift, collisionForklift)).thenReturn(true);
    doCallRealMethod()
        .when(headOnCollisionsMock)
        .setCollisions(collisionForklift, collisionForklift);
    headOnCollisionsMock.setCollisions(collisionForklift, collisionForklift);

    verify(collisionForklift, times(1)).addStop();
    verify(collisionForklift, times(1)).setRecalculate(any(Position.class));
  }
}
