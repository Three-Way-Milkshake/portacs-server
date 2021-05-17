package it.unipd.threewaymilkshake.portacs.server.engine;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;

import java.io.File;
import java.io.FileNotFoundException;
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
public class SimplePointTest {
    

    @Test
    @DisplayName("Tests if distance between two SimplePoint which lay on the same x-axis is computed correctly")
    public void calculateDistanceSameXAxisTest() {
        SimplePoint first = new SimplePoint(1,1);
        SimplePoint second = new SimplePoint(1,5);
        int returned = first.calculateDistance(second);
        assertEquals(4,returned);
    }

    @Test
    @DisplayName("Tests if distance between two SimplePoint which lay on the same y-axis is computed correctly")
    public void calculateDistanceSameYAxisTest() {
        SimplePoint first = new SimplePoint(5,1);
        SimplePoint second = new SimplePoint(1,1);
        int returned = first.calculateDistance(second);
        assertEquals(4,returned);
    }

    
}
