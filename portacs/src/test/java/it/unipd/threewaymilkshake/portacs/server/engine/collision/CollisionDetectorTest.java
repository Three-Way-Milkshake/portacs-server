/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CollisionDetectorTest {

    @Mock
    WarehouseMap warehouseMap;

    CollisionDetector collisionDetector;



    @Test
    @DisplayName("Tests if collision detection works as expected")
    public void processTest() { //TODO: più test di integrazione che di unità?
        collisionDetector = new CollisionDetector();
        ForkliftsList forkliftsList = mock(ForkliftsList.class);
        warehouseMap = mock(WarehouseMap.class);
        ReflectionTestUtils.setField(collisionDetector, "warehouseMap", warehouseMap);
        Map<String,List<SimplePoint>> input = new HashMap<String,List<SimplePoint>>();
        input.put("1", Arrays.asList(new SimplePoint(3,3),new SimplePoint(4,3),new SimplePoint(5,3)));
        input.put("2", Arrays.asList(new SimplePoint(5,1),new SimplePoint(5,2),new SimplePoint(5,3)));
        input.put("3", Arrays.asList(new SimplePoint(4,5),new SimplePoint(4,4),new SimplePoint(4,3)));
        input.put("4", Arrays.asList(new SimplePoint(4,1),new SimplePoint(4,2),new SimplePoint(4,3)));
        when(forkliftsList.getAllNextPositions(2)).thenReturn(input);
        when(warehouseMap.getRows()).thenReturn(10);
        when(warehouseMap.getColumns()).thenReturn(10);

        Map<SimplePoint,List<String>> collisions = collisionDetector.process(forkliftsList);
        Map<SimplePoint,List<String>> returned = new HashMap<SimplePoint,List<String>>();
        returned.put(new SimplePoint(4,3), Arrays.asList("1","3","4"));
        returned.put(new SimplePoint(5,3), Arrays.asList("1","2"));
        assertEquals(returned,collisions);
    }
}
