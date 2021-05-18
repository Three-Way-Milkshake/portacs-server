/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import java.util.concurrent.LinkedBlockingDeque;

public class TasksSequence {
  private LinkedBlockingDeque<Long> tasks;

  TasksSequence(LinkedBlockingDeque<Long> tasks) {
    this.tasks = tasks;
  }

  TasksSequence() {
    this.tasks = new LinkedBlockingDeque<>();
  }

  public TasksSequence(long sth) {
    this();
    tasks.add(sth);
  }

  public boolean isEmpty() {
    return tasks.size() == 0;
  }

  public int size() {
    return tasks.size();
  }

  public void addTask(long t) {
    tasks.addLast(t);
  }

  /**
   * Retrieves AND remove the next tasks
   *
   * @return the task that has been removed
   */
  public long extractNext() {
    return tasks.removeFirst();
  }

  /**
   * Retrieves but does not remove the next task
   *
   * @return the next task
   */
  public long getNext() {
    return tasks.getFirst();
  }

  /**
   * @return A string represented the tasks contained in this sequence as Three Way Protocol
   *     (LIST,1,2,3;)
   */
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append("LIST,");
    b.append(tasks.toString().replaceAll("( |\\[|\\])", ""));
    b.append(';');
    return b.toString();
  }
}
