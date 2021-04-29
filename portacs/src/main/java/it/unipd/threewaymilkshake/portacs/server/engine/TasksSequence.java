/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class TasksSequence {
  private LinkedBlockingDeque<Long> tasks;

  TasksSequence(LinkedBlockingDeque<Long> tasks) {
    this.tasks = tasks;
  }

  TasksSequence() {
    this.tasks = new LinkedBlockingDeque<>();
  }

  public boolean isEmpty(){
    return tasks.size()==0;
  }

  public int size(){
    return tasks.size();
  }

  public void addTask(long t) {
    tasks.addLast(t);
  }

  public long extractNext() {
    return tasks.removeFirst();
  }

  public long getNext() {
    return tasks.getFirst();
  }

  public String toString(){
    return tasks.toString().replaceAll("( |\\[|\\])", "");
  }
}
