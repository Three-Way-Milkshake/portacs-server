package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequence;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.*;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CollisionDetectorTest {
    
    CollisionDetector collisionDetector;

    @Autowired
    @Qualifier("jsonForkliftTest")
    private ForkliftsList forkliftsList;

    @BeforeEach
    public void setUp() {

        collisionDetector = new CollisionDetector();

    }

    @Test
    @DisplayName("Tests work as expected")
    public void getNextPositionsTest() {
        assertEquals(true,false);
    }
}