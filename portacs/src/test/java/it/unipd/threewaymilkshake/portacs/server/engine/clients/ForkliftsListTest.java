/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.persistency.ForkliftDao;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ForkliftsListTest {
  private static ForkliftsList fList;
  private static ForkliftDao fDao;

  @BeforeAll
  public static void setUp() {
    List<Forklift> list = new ArrayList<>();
    Forklift f1 = mock(Forklift.class), f2 = mock(Forklift.class);

    when(f1.isActive()).thenReturn(true);
    when(f2.isActive()).thenReturn(true);
    when(f1.getId()).thenReturn("f1");
    when(f2.getId()).thenReturn("f2");
    when(f1.getToken()).thenReturn("abc");
    when(f2.getToken()).thenReturn("def");
    when(f1.getPositionString()).thenReturn("0,0,0");
    when(f2.getPositionString()).thenReturn("2,3,2");
    when(f1.getTasksString()).thenReturn("3,1,2,3");
    when(f2.getTasksString()).thenReturn("5,4,7,12,9");

    list.add(f1);
    list.add(f2);
    fDao = mock(ForkliftDao.class);
    when(fDao.readForklifts()).thenReturn(list);

    fList = new ForkliftsList(fDao, null, null, null);
  }

  @Test
  public void testForkliftsPositionToString() {
    /* List<Forklift> fList=new ArrayList<>();
    Forklift f1=mock(Forklift.class), f2=mock(Forklift.class);

    when(f1.getId()).thenReturn("f1");
    when(f2.getId()).thenReturn("f2");
    when(f1.getPositionString()).thenReturn("0,0,0");
    when(f2.getPositionString()).thenReturn("2,3,2");

    fList.add(f1);
    fList.add(f2);
    ForkliftDao fDao=mock(ForkliftDao.class);
    when(fDao.readfForklifts()).thenReturn(fList);

    ForkliftsList list=new ForkliftsList(fDao); */

    assertEquals("UNI,2,f1,0,0,0,f2,2,3,2;", fList.getActiveForkliftsPositions());
  }

  @Test
  public void testForkliftsAndTokensToString() {
    assertEquals("LISTF,2,f1,abc,f2,def;", fList.getForkliftsAndTokensString());
  }

  @Test
  public void testGetForkliftsTasks() {
    assertEquals("LIST,2,f1,3,1,2,3,f2,5,4,7,12,9;", fList.getActiveForkliftsTasks());

    List<Forklift> list = new ArrayList<>();
    Forklift f1 = mock(Forklift.class), f2 = mock(Forklift.class);

    when(f1.isActive()).thenReturn(false);
    when(f2.isActive()).thenReturn(false);
    when(f1.getId()).thenReturn("f1");
    when(f2.getId()).thenReturn("f2");
    when(f1.getToken()).thenReturn("abc");
    when(f2.getToken()).thenReturn("def");
    when(f1.getPositionString()).thenReturn("0,0,0");
    when(f2.getPositionString()).thenReturn("2,3,2");
    when(f1.getTasksString()).thenReturn("3,1,2,3");
    when(f2.getTasksString()).thenReturn("5,4,7,12,9");

    list.add(f1);
    list.add(f2);
    ForkliftDao fDao2 = mock(ForkliftDao.class);
    when(fDao2.readForklifts()).thenReturn(list);

    ForkliftsList fListEmpty = new ForkliftsList(fDao2, null, null, null);
    assertEquals("LIST,0;", fListEmpty.getActiveForkliftsTasks());

    when(f1.isActive()).thenReturn(true);
    when(f2.isActive()).thenReturn(true);
    when(f1.getTasksString()).thenReturn("0");
    when(f2.getTasksString()).thenReturn("0");

    assertEquals("LIST,2,f1,0,f2,0;", fListEmpty.getActiveForkliftsTasks());
  }

  @Test
  public void testTokenGeneration() {
    ForkliftsList l = mock(ForkliftsList.class, Mockito.CALLS_REAL_METHODS);
    assertNotNull(l.generateRandomToken());
  }
}
