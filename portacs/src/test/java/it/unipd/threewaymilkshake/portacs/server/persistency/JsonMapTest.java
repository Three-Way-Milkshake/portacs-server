package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.map.CellType;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.engine.map.Poi;


public class JsonMapTest {
    
    private JsonMap jsonMap;

    private WarehouseMap warehouseMap;
    
    @BeforeEach
    public void setUp() {
        jsonMap = new JsonMap("src/main/java/it/unipd/threewaymilkshake/portacs/server/database/map.json");
        
        CellType[][] templateMapItem = {
                { CellType.OBSTACLE, CellType.NEUTRAL, CellType.UP, CellType.RIGHT },
                { CellType.DOWN, CellType.LEFT, CellType.POI, CellType.POI },
                { CellType.POI, CellType.OBSTACLE, CellType.LEFT, CellType.LEFT },
                };

        List<Poi> pois = new LinkedList<>();
        Poi firstPoi = new Poi(1L,"firstPoi",new SimplePoint(1,2));
        Poi secondPoi = new Poi(2L,"secondPoi",new SimplePoint(1,2));
        Poi thirdPoi = new Poi(3L,"thirdPoi",new SimplePoint(2,1));
        pois.add(firstPoi);
        pois.add(secondPoi);
        pois.add(thirdPoi);
                        
        warehouseMap = new WarehouseMap(templateMapItem,pois,null);                
    }

    @Test
    @DisplayName("Test of update")
    public void updateMapTest() {
        jsonMap.updateMap(warehouseMap);
        
        Scanner created;
        Scanner compare;
        try {
            created = new Scanner(new File(jsonMap.getFilePath()),"UTF-8");
            String createdContent = created.useDelimiter("\\Z").next();
            compare = new Scanner(new File("src/test/java/it/unipd/threewaymilkshake/portacs/server/database/mapTest.json"),"UTF-8");
            String comparedContent = compare.useDelimiter("\\Z").next();           
            assertEquals(createdContent,comparedContent);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
        
    }





}
