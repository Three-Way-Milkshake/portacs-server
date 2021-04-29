package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;

class CollisionCell {
    List<String> IDMuletti = new LinkedList<String>();

    void addForklift(String id) {
        IDMuletti.add(id);
    }
}

class CollisionMap {
    private CollisionCell[][] map;

    public CollisionMap(int rows, int columns) {
        map = new CollisionCell[rows][columns];
    }
    

    public CollisionMap sum(HashMap<String,SimplePoint> nextMoves) {
        for(String key : nextMoves.keySet()) 
        {   
            SimplePoint position = nextMoves.get(key);
            map[position.getX()][position.getY()].addForklift(key);
        }
        return this;

    }
    

} 

public class CollisionDetection {


    static public void collisionChecker(ForkliftsList forklifts, int rows, int columns) {
        Map<String,SimplePoint> actualPositions = forklifts.getAllNextPositions(0);
        Map<String,SimplePoint> firstNextMoves = forklifts.getAllNextPositions(1);
        Map<String,SimplePoint> secondNextMoves = forklifts.getAllNextPositions(2);

        CollisionMap collisionSum = new CollisionMap(rows,columns);
        /*collisionSum.sum(actualPositions)
            .sum(firstNextMoves)
            .sum(secondNextMoves);

        collisionSum.getCollision();*/
    

    }
    
}
