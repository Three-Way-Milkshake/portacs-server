package it.unipd.threewaymilkshake.portacs.server.engine.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.AbstractLocation;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Manager;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TestStrategyBreadthFirst {
  private PathFindingStrategy strategy=new StrategyBreadthFirst();

  @Test
  public void testSimplePath(){
    int[][] map={
      {1,0,1},
      {1,0,1},
      {1,1,1}
    };
    AbstractLocation s1=new Position(0, 0, Orientation.UP), e1=new SimplePoint(0, 2);
    List<Move> p1=strategy.getPath(map, s1, e1);
    List<Integer> l1=p1.stream()
      .map(m->m.ordinal())
      .collect(Collectors.toList());
    assertEquals("[1,0,0,2,0,0,2,0,0]", l1.toString());
  }
}
