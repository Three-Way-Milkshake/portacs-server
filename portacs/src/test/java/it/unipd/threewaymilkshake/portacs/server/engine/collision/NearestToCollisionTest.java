package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.*;

import org.assertj.core.util.Arrays;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class NearestToCollisionTest {

    @Test
    @DisplayName("Tests if the stops are assigned to the right unit: no unit is already in stop")
    public void nearestCanGoTest() {
        Forklift f1 = new Forklift("1","asfdsfd");
        f1.setPosition(new Position(1,2,Orientation.DOWN));
        Forklift f2 = new Forklift("2","asfdsfd");
        f2.setPosition(new Position(2,4,Orientation.LEFT));

        CollisionForklift first = new CollisionForklift(f1);
        CollisionForklift second = new CollisionForklift(f2);

        Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
        collisions.put(new SimplePoint(2,2),new LinkedList<>(List.of(first,second)));
        
        NearestToCollision nearestToCollision = new NearestToCollision();
        nearestToCollision.process(collisions);

        assertEquals(1,second.getStops());

    }

    @Test
    @DisplayName("Tests if the stops are assigned to the right unit: the farthest unit is already in stop")
    public void nearestCanGoButFarthesIsAlreadyInStopTest() {
        Forklift f1 = new Forklift("1","asfdsfd");
        f1.setPosition(new Position(1,2,Orientation.DOWN));
        Forklift f2 = new Forklift("2","asfdsfd");
        f2.setPosition(new Position(2,4,Orientation.LEFT));

        CollisionForklift first = new CollisionForklift(f1);
        CollisionForklift second = new CollisionForklift(f2);
        second.addStop();

        Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
        collisions.put(new SimplePoint(2,2),new LinkedList<>(List.of(first,second)));
        
        NearestToCollision nearestToCollision = new NearestToCollision();
        nearestToCollision.process(collisions);

        assertEquals(1,second.getStops());

    }

    @Test
    @DisplayName("Tests if the stops are assigned to the right unit: the farthest unit is already recalculating")
    public void nearestCanGoButFarthesIsAlreadyRecalculatingTest() {
        Forklift f1 = new Forklift("1","asfdsfd");
        f1.setPosition(new Position(1,2,Orientation.DOWN));
        Forklift f2 = new Forklift("2","asfdsfd");
        f2.setPosition(new Position(2,4,Orientation.LEFT));

        CollisionForklift first = new CollisionForklift(f1);
        CollisionForklift second = new CollisionForklift(f2);
        second.setRecalculate(new Position(2,3,Orientation.UP));

        Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
        collisions.put(new SimplePoint(2,2),new LinkedList<>(List.of(first,second)));
        
        NearestToCollision nearestToCollision = new NearestToCollision();
        nearestToCollision.process(collisions);

        assertEquals(0,second.getStops());

    }

    @Test
    @DisplayName("Tests if the stops are assigned to the right unit: the units are equally distant from the collision point")
    public void unitsEquallyDistantFromCollisionPointTest() {
        Forklift f1 = new Forklift("1","asfdsfd");
        f1.setPosition(new Position(0,2,Orientation.DOWN));
        Forklift f2 = new Forklift("2","asfdsfd");
        f2.setPosition(new Position(2,4,Orientation.LEFT));

        CollisionForklift first = new CollisionForklift(f1);
        CollisionForklift second = new CollisionForklift(f2);

        Map<SimplePoint, List<CollisionForklift>> collisions = new HashMap<>();
        collisions.put(new SimplePoint(2,2),new LinkedList<>(List.of(first,second)));
        
        NearestToCollision nearestToCollision = new NearestToCollision();
        nearestToCollision.process(collisions);
        assertEquals(true,(second.isInStop() || first.isInStop()));

    }
}