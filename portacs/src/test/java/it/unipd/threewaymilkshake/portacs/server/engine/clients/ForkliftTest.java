package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Orientation;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequence;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
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
public class ForkliftTest {
    
    Forklift forklift;

    @BeforeEach
    public void setUp() {
        forklift = new Forklift("forklift","abcdefghi1234");
        forklift.setPosition(new Position(3,4,Orientation.RIGHT));
        List<Move> pathToNextTask = Arrays.asList(Move.GOSTRAIGHT,Move.TURNLEFT,Move.GOSTRAIGHT);
        forklift.setPathToNextTask(pathToNextTask);
    }

    @Test
    @DisplayName("Tests if getNextPositions work as expected")
    public void getNextPositionsTest() {
        List<SimplePoint> returned = forklift.getNextPositions(2);
        List<SimplePoint> toCompare = Arrays.asList(new SimplePoint(3,4),new SimplePoint(3,5),new SimplePoint(3,5),new SimplePoint(3,5));
        //System.out.println("*****************************************************");        
        IntStream.range(0, returned.size())
        .forEach(
            i -> {             
              assertEquals(returned.get(i),toCompare.get(i));             
            });
        //System.out.println("*****************************************************");
    }
}
