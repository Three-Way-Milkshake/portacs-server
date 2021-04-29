package it.unipd.threewaymilkshake.portacs.server.engine.clients;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Manager;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;
import it.unipd.threewaymilkshake.portacs.server.persistency.ForkliftDao;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ForkliftsListTest {
  private static ForkliftsList fList;
  private static ForkliftDao fDao;
  
  @BeforeAll
  public static void setUp(){
    List<Forklift> list=new ArrayList<>();
    Forklift f1=mock(Forklift.class), f2=mock(Forklift.class);
    
    when(f1.getId()).thenReturn("f1");
    when(f2.getId()).thenReturn("f2");
    when(f1.getToken()).thenReturn("abc");
    when(f2.getToken()).thenReturn("def");
    when(f1.getPositionString()).thenReturn("0,0,0");
    when(f2.getPositionString()).thenReturn("2,3,2");

    list.add(f1);
    list.add(f2);
    fDao=mock(ForkliftDao.class);
    when(fDao.readfForklifts()).thenReturn(list);

    fList=new ForkliftsList(fDao);
  }

  @Test
  public void testForkliftsPositionToString(){
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

    assertEquals("UNI,2,f1,0,0,0,f2,2,3,2;", fList.getForkliftsPositions());
  }

  @Test
  public void testForkliftsAndTokensToString(){
    assertEquals("LISTF,2,f1,abc,f2,def;", fList.getForkliftsAndTokensString());
  }
}
