package it.unipd.threewaymilkshake.portacs.server.engine;

import java.util.ArrayDeque;
import java.util.Deque;

public class TasksSequence{
  private Deque<Long> tasks;

  TasksSequence(Deque<Long> tasks){
    this.tasks=tasks;
  }

  TasksSequence(){
    this.tasks=new ArrayDeque<>();
  }

  public void addTask(long t){
    tasks.addLast(t);
  }

  public long extractNext(){
    return tasks.removeFirst();
  }

  public long getNext(){
    return tasks.getFirst();
  }
}