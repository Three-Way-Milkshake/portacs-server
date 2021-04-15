package it.unipd.threewaymilkshake.portacs.server.engine.map;

import java.util.List;

import it.unipd.threewaymilkshake.portacs.server.engine.AbstractLocation;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;

public interface PathFindingStrategy{
  List<Move> getPath(int[][] map, AbstractLocation start, AbstractLocation end);
}