/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import java.util.stream.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class PositionTest {

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions1() {
    Position start = new Position(1, 1, Orientation.UP);
    Move move = Move.GOSTRAIGHT;
    Position expected = new Position(0, 1, Orientation.UP);
    assertEquals(expected, start.computeNextPosition(move));
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions2() {
    Position start = new Position(1, 1, Orientation.RIGHT);
    Move move = Move.GOSTRAIGHT;
    Position expected = new Position(1, 2, Orientation.UP);
    assertEquals(expected, start.computeNextPosition(move));
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions3() {
    Position start = new Position(1, 1, Orientation.DOWN);
    Move move = Move.GOSTRAIGHT;
    Position expected = new Position(2, 1, Orientation.UP);
    assertEquals(expected, start.computeNextPosition(move));
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions4() {
    Position start = new Position(1, 1, Orientation.LEFT);
    Move move = Move.GOSTRAIGHT;
    Position expected = new Position(1, 0, Orientation.UP);
    assertEquals(expected, start.computeNextPosition(move));
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions5() {
    Position start = new Position(1, 1, Orientation.UP);
    Move move = Move.TURNLEFT;
    Position expected = new Position(1, 1, Orientation.LEFT);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions6() {
    Position start = new Position(1, 1, Orientation.RIGHT);
    Move move = Move.TURNLEFT;
    Position expected = new Position(1, 1, Orientation.UP);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions7() {
    Position start = new Position(1, 1, Orientation.DOWN);
    Move move = Move.TURNLEFT;
    Position expected = new Position(1, 1, Orientation.RIGHT);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions8() {
    Position start = new Position(1, 1, Orientation.LEFT);
    Move move = Move.TURNLEFT;
    Position expected = new Position(1, 1, Orientation.DOWN);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions9() {
    Position start = new Position(1, 1, Orientation.UP);
    Move move = Move.TURNRIGHT;
    Position expected = new Position(1, 1, Orientation.RIGHT);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions10() {
    Position start = new Position(1, 1, Orientation.RIGHT);
    Move move = Move.TURNRIGHT;
    Position expected = new Position(1, 1, Orientation.DOWN);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions11() {
    Position start = new Position(1, 1, Orientation.DOWN);
    Move move = Move.TURNRIGHT;
    Position expected = new Position(1, 1, Orientation.LEFT);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions12() {
    Position start = new Position(1, 1, Orientation.LEFT);
    Move move = Move.TURNRIGHT;
    Position expected = new Position(1, 1, Orientation.UP);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions13() {
    Position start = new Position(1, 1, Orientation.UP);
    Move move = Move.TURNAROUND;
    Position expected = new Position(1, 1, Orientation.DOWN);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions14() {
    Position start = new Position(1, 1, Orientation.RIGHT);
    Move move = Move.TURNAROUND;
    Position expected = new Position(1, 1, Orientation.LEFT);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions15() {
    Position start = new Position(1, 1, Orientation.DOWN);
    Move move = Move.TURNAROUND;
    Position expected = new Position(1, 1, Orientation.UP);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }

  @Test
  @DisplayName("Tests computing next positions")
  public void computeNextPositions16() {
    Position start = new Position(1, 1, Orientation.LEFT);
    Move move = Move.TURNAROUND;
    Position expected = new Position(1, 1, Orientation.RIGHT);
    assertEquals(expected.getOrientation(), start.computeNextPosition(move).getOrientation());
  }
}
