/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.map;

import it.unipd.threewaymilkshake.portacs.server.engine.AbstractLocation;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import java.util.List;

public interface PathFindingStrategy {
  List<Move> getPath(int[][] map, AbstractLocation start, AbstractLocation end);
}
