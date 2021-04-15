package it.unipd.threewaymilkshake.portacs.server.engine;

import java.util.List;

public abstract class Subject{
  private List<Observer> observers;

  public void attach(Observer o){
    observers.add(o);
  }

  public void detach(Observer o){
    observers.remove(o);
  }

  public void notifyObservers(){
    observers.stream().forEach(Observer::update);
  }
}