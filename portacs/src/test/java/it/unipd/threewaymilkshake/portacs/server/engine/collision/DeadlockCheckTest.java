package it.unipd.threewaymilkshake.portacs.server.engine.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
public class DeadlockCheckTest {

    @Mock
    Forklift forklift;

    @Mock
    CollisionForklift collisionForklift;

    @Mock
    Position position;


    @Test
    @DisplayName("Tests is the anti deadlock system works with a stall of 6 turns")
    public void criticalDeadlockWith6TurnsStallTest() {
        forklift = mock(Forklift.class);
        when(forklift.isInDeadlock(6)).thenReturn(true);
        when(forklift.getId()).thenReturn("1");
        when(forklift.getNumberOfStalls()).thenReturn(6);

        CollisionForklift first = new CollisionForklift(forklift);
        List<CollisionForklift> input = new LinkedList<CollisionForklift>();
        input.add(first);

        DeadlockCheck deadlockCheck = new DeadlockCheck();
        deadlockCheck.process(input);

        verify(forklift, times(1)).addExceptionalEvent("Il muletto 1 è in stallo. Probabilmente è richiesto l'intervento dell'operatore");
    }

    @Test
    @DisplayName("Tests is the anti deadlock system works with a stall of 3 turns")
    public void alertDeadlockWith3TurnsStallTest() {
        forklift = mock(Forklift.class);

        collisionForklift = mock(CollisionForklift.class);
        position = mock(Position.class);
        when(forklift.isInDeadlock(3)).thenReturn(true);
        when(forklift.getId()).thenReturn("1");
        when(forklift.getNumberOfStalls()).thenReturn(3);
        when(forklift.getPosition()).thenReturn(position);
        when(forklift.getNextPositions(1)).thenReturn(new LinkedList<>(List.of(new SimplePoint(1,1),new SimplePoint(1,2))));
        when(position.getPoint()).thenReturn(new SimplePoint(1,1));


        when(collisionForklift.getForklift()).thenReturn(forklift);
        List<CollisionForklift> input = new LinkedList<CollisionForklift>();
        input.add(collisionForklift);

        DeadlockCheck deadlockCheck = new DeadlockCheck();
        deadlockCheck.process(input);

        verify(collisionForklift, times(1)).setRecalculate(anyList());
    }

}