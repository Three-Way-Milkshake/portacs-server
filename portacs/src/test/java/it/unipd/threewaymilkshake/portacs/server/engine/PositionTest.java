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

public class PositionTest {


    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions1() {
        Position start = new Position(1,1,Orientation.UP);
        Move move = Move.GOSTRAIGHT;
        Position expected = new Position(0,1,Orientation.UP);
        assertEquals(expected,start.computeNextPosition(move));
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions2() {
        Position start = new Position(1,1,Orientation.RIGHT);
        Move move = Move.GOSTRAIGHT;
        Position expected = new Position(1,2,Orientation.UP);
        assertEquals(expected,start.computeNextPosition(move));
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions3() {
        Position start = new Position(1,1,Orientation.DOWN);
        Move move = Move.GOSTRAIGHT;
        Position expected = new Position(2,1,Orientation.UP);
        assertEquals(expected,start.computeNextPosition(move));
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions4() {
        Position start = new Position(1,1,Orientation.LEFT);
        Move move = Move.GOSTRAIGHT;
        Position expected = new Position(1,0,Orientation.UP);
        assertEquals(expected,start.computeNextPosition(move));
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions5() {
        Position start = new Position(1,1,Orientation.UP);
        Move move = Move.TURNLEFT;
        Position expected = new Position(1,1,Orientation.LEFT);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions6() {
        Position start = new Position(1,1,Orientation.RIGHT);
        Move move = Move.TURNLEFT;
        Position expected = new Position(1,1,Orientation.UP);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions7() {
        Position start = new Position(1,1,Orientation.DOWN);
        Move move = Move.TURNLEFT;
        Position expected = new Position(1,1,Orientation.RIGHT);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions8() {
        Position start = new Position(1,1,Orientation.LEFT);
        Move move = Move.TURNLEFT;
        Position expected = new Position(1,1,Orientation.DOWN);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions9() {
        Position start = new Position(1,1,Orientation.UP);
        Move move = Move.TURNRIGHT;
        Position expected = new Position(1,1,Orientation.RIGHT);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions10() {
        Position start = new Position(1,1,Orientation.RIGHT);
        Move move = Move.TURNRIGHT;
        Position expected = new Position(1,1,Orientation.DOWN);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions11() {
        Position start = new Position(1,1,Orientation.DOWN);
        Move move = Move.TURNRIGHT;
        Position expected = new Position(1,1,Orientation.LEFT);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions12() {
        Position start = new Position(1,1,Orientation.LEFT);
        Move move = Move.TURNRIGHT;
        Position expected = new Position(1,1,Orientation.UP);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions13() {
        Position start = new Position(1,1,Orientation.UP);
        Move move = Move.TURNAROUND;
        Position expected = new Position(1,1,Orientation.DOWN);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }
    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions14() {
        Position start = new Position(1,1,Orientation.RIGHT);
        Move move = Move.TURNAROUND;
        Position expected = new Position(1,1,Orientation.LEFT);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions15() {
        Position start = new Position(1,1,Orientation.DOWN);
        Move move = Move.TURNAROUND;
        Position expected = new Position(1,1,Orientation.UP);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }

    @Test
    @DisplayName("Tests computing next positions")
    public void computeNextPositions16() {
        Position start = new Position(1,1,Orientation.LEFT);
        Move move = Move.TURNAROUND;
        Position expected = new Position(1,1,Orientation.RIGHT);
        assertEquals(expected.getOrientation(),start.computeNextPosition(move).getOrientation());
    }
    
}
