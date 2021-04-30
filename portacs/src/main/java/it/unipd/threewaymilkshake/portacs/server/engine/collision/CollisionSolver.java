/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;


class Action {
    public List<String> actions; //STOP o RICALCOLO
    public SimplePoint obstacle;

    public void add(String toAdd) {
        actions.add(toAdd);
    }

    public Action(List<String> actions) {
        this.actions = actions;
        this.obstacle = null;
    }


    public boolean isInStop() {
        boolean found = false;
        for(String action : actions) 
        {
            if(action == "STOP")
                found = true;
        }
        return found;
    }

    public boolean isCalculatingAgaing() {
        boolean found = false;
        for(String action : actions) 
        {
            if(action == "RICALCOLO")
                found = true;
        }
        return found;
    }

    /*public void printList() {
        for(String action : actions) 
        {
            System.out.printf(action + " ");

        }
        System.out.printf(((obstacle == null) ? " / " : obstacle.printPosition()));
    }*/

    
}

public class CollisionSolver implements Handler<Map<SimplePoint,List<String>>,Map<String, Action>> {
    
    private Map<String,Action> response; //responso
    private Map<SimplePoint,List<String>> collisions; //input: signalled collisions
    @Autowired
    private ForkliftsList forkliftsList;

    public void checkNumberOfCollisions() {
        HashMap<String,Integer> numberOfCollision = new HashMap<String,Integer>();
        for(SimplePoint key : collisions.keySet()) 
        {
            for(String unit : collisions.get(key)) 
            {
                Integer j = numberOfCollision.get(unit);
                numberOfCollision.put(unit,(j == null) ? 1 : j + 1);
            }
            
        }


        for(String key : numberOfCollision.keySet()) 
        {
            //System.out.println(key.id + ":" + numberOfCollision.get(key));
            
            response.put(key,new Action(new LinkedList<String>()));

            if(numberOfCollision.get(key) > 1) {
                response.get(key).add("STOP");
            }
                
        }


    }

    public void checkHeadOnCollision() {
        
        for(SimplePoint key : collisions.keySet()) 
        {
            for(int i = 0; i < collisions.get(key).size() - 1; i++)
            {
                for(int j = i + 1; j < collisions.get(key).size(); j++)
                {
                    //System.out.println(collisions.get(key).get(i).id + " " + collisions.get(key).get(j).id);
                    setCollisions(collisions.get(key).get(i),collisions.get(key).get(j));
                }
            }

        }
    }

    public void setCollisions(String a, String b) {
        if(forkliftsList.headOnRisk(a,b)) { // c'è rischio frontale
            if(response.get(a).isInStop()) {
                response.get(a).add("STOP");
                response.get(a).add("STOP");
                response.get(b).add("RICALCOLO");
                response.get(b).obstacle = forkliftsList.getSimplePointFromString(a);
            }
            else if(response.get(b).isInStop()) {
                response.get(b).add("STOP");
                response.get(b).add("STOP");
                response.get(a).add("RICALCOLO");
                response.get(a).obstacle = forkliftsList.getSimplePointFromString(b);
            }
            else {
                Random rand = new Random(); 
                int random = rand.nextInt(1); 
                if(random == 0) {
                    response.get(a).add("STOP");
                    response.get(a).add("STOP");
                    response.get(a).add("STOP");
                    response.get(b).add("RICALCOLO");
                    response.get(b).obstacle = forkliftsList.getSimplePointFromString(a);;
                }
                else {
                    response.get(b).add("STOP");
                    response.get(b).add("STOP");
                    response.get(b).add("STOP");
                    response.get(a).add("RICALCOLO");
                    response.get(a).obstacle = forkliftsList.getSimplePointFromString(a);;
                }
            }
        }
    }
    
    public void setNearest() {
        for(SimplePoint key : collisions.keySet()) 
        {
            int min = Integer.MAX_VALUE;
            for(String unit : collisions.get(key)) 
            {
                int distance = forkliftsList.getSimplePointFromString(unit).calculateDistance(key);
                    // se distanza > 0 && !stop && !ricalcolo
                if(distance == 0 || !response.get(unit).isInStop() && !response.get(unit).isCalculatingAgaing()) {
                    if(distance <= min)
                        min = distance;
                }
            }
            List<String> equals = new LinkedList<String>();
            for(String unit : collisions.get(key)) 
            {             
                int distance = forkliftsList.getSimplePointFromString(unit).calculateDistance(key);
                    if(!response.get(unit).isInStop() && !response.get(unit).isCalculatingAgaing()) {
                        if(distance > min) {
                            
                            response.get(unit).add("STOP");
                        }
                        else if(distance == min) {
                            equals.add(unit);
                        }
                }
            }
            if(equals.size() > 1) {
                
                Random rand = new Random(); 
                int random = rand.nextInt(equals.size());
                for(int i = 0; i < equals.size(); i++) {
                    if(i != random)
                        response.get(equals.get(i)).add("STOP");
                }
            
            }
        }
    }

    @Override
    public Map<String, Action> process(Map<SimplePoint, List<String>> input) {
        collisions = input;
        response = new HashMap<String,Action>();

        checkNumberOfCollisions();
        checkHeadOnCollision();
        setNearest();

        return response;
    }





}




