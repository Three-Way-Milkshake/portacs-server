package it.unipd.threewaymilkshake.portacs.server.engine;

public class Position extends AbstractLocation{
  private Orientation orientation;
  
  public Position(int x, int y, Orientation o) {
    super(x, y);
    orientation=o;
  }

  public Position(Position p){
    super(p);
    orientation=((Position)p).orientation;
  }
  
  public Orientation getOrientation() {
    return orientation;
  }

  public void setPosition(int x, int y, Orientation o){
    setX(x);
    setY(y);
    orientation=o;
  }
}