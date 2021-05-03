/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;


class CollisionCell {

    Set<String> collisionsForCell;
     
    public CollisionCell() {
        this.collisionsForCell = new LinkedHashSet<String>();
    }

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
    
    public void sum(Map<String,List<SimplePoint>> nextMoves) {
        for(String key : nextMoves.keySet()) 
        {   
            List<SimplePoint> positions = nextMoves.get(key);
            for(SimplePoint point : positions) {

                if(map[point.getX()][point.getY()] == null) {
                    map[point.getX()][point.getY()] = new CollisionCell();

                }
                map[point.getX()][point.getY()].addForklift(key);
            }
        }
    }


    public Map<SimplePoint,List<String>> getCollisions() {
        Map<SimplePoint,List<String>> toReturn = new HashMap<SimplePoint,List<String>>();
        int rows = map.length, cols = map[0].length;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {

                if(map[i][j] != null) {                   
                    Set<String> collisionsForCell = map[i][j].getCollisionsForCell();
                    List<String> listToReturn = new LinkedList<String>();
                    listToReturn.addAll(collisionsForCell);
                    if(listToReturn.size() > 1) {
                        toReturn.put(new SimplePoint(i,j),listToReturn);
                    }
                }

            }
        }
        return toReturn;
        
  }


}


public class CollisionDetector implements Handler<ForkliftsList,Map<SimplePoint,List<String>>> {

    static int NUMBER_OF_FUTURE_MOVES = 2;
    // @Autowired 
    private WarehouseMap warehouseMap;
    
    public Map<SimplePoint,List<String>> process(ForkliftsList forklifts) {

        int rows = warehouseMap.getRows();
        int columns = warehouseMap.getColumns();
        System.out.println("+++++++++++" + rows + " " + columns);
        Map<String,List<SimplePoint>> nextPositions = forklifts.getAllNextPositions(NUMBER_OF_FUTURE_MOVES);        
        CollisionMap collisionSum = new CollisionMap(rows,columns);
        collisionSum.sum(nextPositions);
        return collisionSum.getCollisions();

    }

    public CollisionDetector setWarehouseMap(WarehouseMap warehouseMap){
        this.warehouseMap=warehouseMap;
        return this;
    }
    
}
