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

    Set<CollisionForklift> collisionsForCell;
     
    public CollisionCell() {
        this.collisionsForCell = new LinkedHashSet<CollisionForklift>();
    }

    public Set<CollisionForklift> getCollisionsForCell() {
        return collisionsForCell;
    }

  

  void addForklift(CollisionForklift id) {
    collisionsForCell.add(id);
  }
}

class CollisionMap {
    private CollisionCell[][] map; 

    public CollisionMap(int rows, int columns) {
        map = new CollisionCell[rows][columns];
    }
    
    public void sum(Map<CollisionForklift,List<SimplePoint>> nextMoves) {
        for(CollisionForklift key : nextMoves.keySet()) 
        {   
            List<SimplePoint> positions = nextMoves.get(key);
            for(SimplePoint point : positions) {
                //System.out.println("POSIZIONEEEE " +point.getX() + ";" + point.getY());

                if(point.getX() < map.length && point.getY() < map[0].length) {
                    
                    if(map[point.getX()][point.getY()] == null) {
                        map[point.getX()][point.getY()] = new CollisionCell();

                    }
                    map[point.getX()][point.getY()].addForklift(key);
                }

                
            }
        }
    }


    public Map<SimplePoint,List<CollisionForklift>> getCollisions() {
        Map<SimplePoint,List<CollisionForklift>> toReturn = new HashMap<SimplePoint,List<CollisionForklift>>();
        int rows = map.length, cols = map[0].length;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {

                if(map[i][j] != null) {                   
                    Set<CollisionForklift> collisionsForCell = map[i][j].getCollisionsForCell();
                    List<CollisionForklift> listToReturn = new LinkedList<CollisionForklift>();
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


public class CollisionDetection implements Handler<List<CollisionForklift>,Map<SimplePoint,List<CollisionForklift>>> {

    static int NUMBER_OF_FUTURE_MOVES = 2;
    // @Autowired 
    private WarehouseMap warehouseMap;

    
    public Map<SimplePoint,List<CollisionForklift>> process(List<CollisionForklift> forklifts) {

        int rows = warehouseMap.getRows();
        int columns = warehouseMap.getColumns();
        // System.out.println("+++++++++++" + rows + " " + columns);

        Map<CollisionForklift, List<SimplePoint>> nextPositions = new HashMap<CollisionForklift, List<SimplePoint>>();

        for (CollisionForklift key : forklifts) {
            if(key.getForklift().isActive()) {
                nextPositions.put(key, key.getForklift().getNextPositions(NUMBER_OF_FUTURE_MOVES));
            }
        }
        
        for(CollisionForklift key : nextPositions.keySet()) {
            System.out.println("Muletto " + key.getForklift().getId());
            key.getForklift().printNextMoves();
            for(SimplePoint s : nextPositions.get(key)) {
                System.out.print(" " + s.getX() + " " + s.getY() + " -- ");
            }
            System.out.print("\n");
        }

        CollisionMap collisionSum = new CollisionMap(rows,columns);
        collisionSum.sum(nextPositions);
        return collisionSum.getCollisions();

    }

    public CollisionDetection setWarehouseMap(WarehouseMap warehouseMap){
        this.warehouseMap=warehouseMap;
        return this;
    }

  
    
}
