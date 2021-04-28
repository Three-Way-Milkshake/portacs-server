/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.AbstractLocation;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class StrategyBreadthFirstTest {
  private PathFindingStrategy strategy = new StrategyBreadthFirst();

  /*
   * 0 -> GOSTRAIGHT,
   * 1 -> TURNAROUND,
   * 2 -> TURNRIGHT,
   * 3 -> TURNLEFT,
   * 4 -> STOP
   */

  @Test
  public void testSimplePath1() {
    int[][] map = {
      {1, 0, 1},
      {1, 0, 1},
      {1, 1, 1}
    };
    AbstractLocation s1 = new Position(0, 0, Orientation.UP), e1 = new SimplePoint(0, 2);
    List<Move> p1 = strategy.getPath(map, s1, e1);
    List<Integer> l1 = p1.stream().map(m -> m.ordinal()).collect(Collectors.toList());
    assertEquals("[1, 0, 0, 3, 0, 0, 3, 0, 0]", l1.toString());
  }

  @Test
  public void testSimplePath2() {
    int[][] map = {
      {1, 0, 1},
      {1, 1, 1},
      {1, 0, 1}
    };
    AbstractLocation s1 = new Position(0, 0, Orientation.UP), e1 = new SimplePoint(2, 2);
    List<Move> p1 = strategy.getPath(map, s1, e1);
    List<Integer> l1 = p1.stream().map(m -> m.ordinal()).collect(Collectors.toList());
    assertEquals("[1, 0, 3, 0, 0, 2, 0]", l1.toString());
  }

  @Test
  public void testSimplePath3() {
    int[][] map = {
      {1, 0, 1, 0},
      {1, 0, 1, 1},
      {1, 1, 0, 1},
      {1, 1, 1, 1}
    };
    AbstractLocation s1 = new Position(0, 0, Orientation.UP), e1 = new SimplePoint(1, 2);
    List<Move> p1 = strategy.getPath(map, s1, e1);
    List<Integer> l1 = p1.stream().map(m -> m.ordinal()).collect(Collectors.toList());
    assertEquals("[1, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 3, 0]", l1.toString());
  }
}
