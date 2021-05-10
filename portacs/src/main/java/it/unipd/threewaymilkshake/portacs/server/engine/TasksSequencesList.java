/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class TasksSequencesList {
  private Map<Long, TasksSequence> tasksMap = new HashMap<>();
  private static long NEW_SEQUENCE_ID = 1L;
  private static long NEXT_SEQUENCE_ID = 1L;

  public synchronized long addTasksSequence(LinkedBlockingDeque<Long> pois) {
    TasksSequence t = new TasksSequence(pois);
    System.out.println(
        "************** ADDED TASK LIST with ID(" + NEW_SEQUENCE_ID + "): " + t.toString());
    tasksMap.put(NEW_SEQUENCE_ID, t);
    return NEW_SEQUENCE_ID++;
  }

  public synchronized boolean removeTasksSequence(long id) {
    boolean r = false;
    if (tasksMap.containsKey(id)) {
      r = true;
      tasksMap.remove(id);
    }

    return r;
  }

  public synchronized TasksSequence getTasksSequence() {
    TasksSequence r = null;
    if (!tasksMap.isEmpty()) {
      while (!tasksMap.containsKey(NEXT_SEQUENCE_ID)) {
        ++NEXT_SEQUENCE_ID;
      }
      r = tasksMap.remove(NEXT_SEQUENCE_ID++);
    }
    System.out.println("************** LOOKING FOR TASKS LIST with ID(" + (NEXT_SEQUENCE_ID - 1));

    return r;
  }

  public int size() {
    return tasksMap.size();
  }
}
