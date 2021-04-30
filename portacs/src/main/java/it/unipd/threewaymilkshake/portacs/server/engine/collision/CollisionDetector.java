/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;

class CollisionCell {
    Set<String> collisionsForCell = new LinkedHashSet<String>();

    public Set<String> getCollisionsForCell() {
        return collisionsForCell;
    }

    void addForklift(String id) {
        collisionsForCell.add(id);
    }

}

class CollisionMap {
    private CollisionCell[][] map;

    public CollisionMap(int rows, int columns) {
        map = new CollisionCell[rows][columns];
    }
    
    public CollisionMap sum(Map<String,List<SimplePoint>> nextMoves) {
        for(String key : nextMoves.keySet()) 
        {   
            List<SimplePoint> positions = nextMoves.get(key);
            for(SimplePoint point : positions)
            map[point.getX()][point.getY()].addForklift(key);
        }
        return this;
    }


    public Map<SimplePoint,List<String>> getCollisions() {
        Map<SimplePoint,List<String>> toReturn = new HashMap<SimplePoint,List<String>>();
        int rows = map.length, cols = map[0].length;

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                List<String> collisionsForCell = new LinkedList<String>();
                collisionsForCell.addAll(map[i][j].getCollisionsForCell());
                toReturn.put(new SimplePoint(i,j),collisionsForCell);
            }
        }
        return toReturn;
        
    }
    
} 

public class CollisionDetector implements Handler {

    static int NUMBER_OF_FUTURE_MOVES = 2;

    public Map<SimplePoint,List<String>> process(ForkliftsList forklifts, int rows, int columns) {
        Map<String,List<SimplePoint>> nextPositions = forklifts.getAllNextPositions(NUMBER_OF_FUTURE_MOVES);

        CollisionMap collisionSum = new CollisionMap(rows,columns);
        collisionSum.sum(nextPositions);

        return collisionSum.getCollisions();
    
    }
    
}
