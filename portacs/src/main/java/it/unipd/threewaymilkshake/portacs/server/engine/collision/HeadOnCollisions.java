package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import java.util.List;
import java.util.Map;
import java.util.Random;

import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;




public class HeadOnCollisions implements Handler<Map<SimplePoint,List<CollisionForklift>>,Map<SimplePoint,List<CollisionForklift>>> {

    
    
    public boolean headOnRisk(CollisionForklift first, CollisionForklift second) {
        Position positionFirst = first.getForklift().getPosition();
        Position positionSecond = second.getForklift().getPosition();
        if(positionFirst.getPoint().equals(positionSecond.getPoint())) {
            System.out.println("****************COLLISIONE AVVENUTA***********************");
            first.collisionOccurred();
            second.collisionOccurred();
        }
        else if(positionFirst.getX() == positionSecond.getX()) {
            if(positionFirst.getY() < positionSecond.getY()) {
                return positionFirst.getOrientation() == Orientation.RIGHT && positionSecond.getOrientation() == Orientation.LEFT;
            }
            else if(positionFirst.getY() > positionSecond.getY()) {
                return positionFirst.getOrientation() == Orientation.LEFT && positionSecond.getOrientation() == Orientation.RIGHT;
            }
        }
        else if(positionFirst.getY() == positionSecond.getY()) {
            if(positionFirst.getX() < positionSecond.getX()) {
                return positionFirst.getOrientation() == Orientation.DOWN && positionSecond.getOrientation() == Orientation.UP;
            }
            else if(positionFirst.getX() > positionSecond.getX()) {
                return positionFirst.getOrientation() == Orientation.UP && positionSecond.getOrientation() == Orientation.DOWN;
            }
        }
        return false;
    }

    public void setCollisions(CollisionForklift a, CollisionForklift b) {
        if(headOnRisk(a,b)) { // c'è rischio frontale
            System.out.println("RISCHIO FRONTALE");
            if(a.isInStop()) {
                a.addStop();
                b.setRecalculate(a.getForklift().getPosition());
            }
            else if(b.isInStop()) {
                b.addStop();
                a.setRecalculate(b.getForklift().getPosition());
            }
            else {
                Random rand = new Random(); 
                int random = rand.nextInt(1); 
                if(random == 0) {
                    a.addStop();
                    b.setRecalculate(a.getForklift().getPosition());
                }
                else {
                    b.addStop();
                    a.setRecalculate(b.getForklift().getPosition());
                }
            }
        }
    }


    @Override
    public Map<SimplePoint, List<CollisionForklift>> process(Map<SimplePoint, List<CollisionForklift>> collisions) {

        for(SimplePoint key : collisions.keySet()) 
        {
            for(int i = 0; i < collisions.get(key).size() - 1; i++)
            {
                for(int j = i + 1; j < collisions.get(key).size(); j++)
                {
                    setCollisions(collisions.get(key).get(i),collisions.get(key).get(j));
                }
            }

        }

        return collisions;
    }
    
}
