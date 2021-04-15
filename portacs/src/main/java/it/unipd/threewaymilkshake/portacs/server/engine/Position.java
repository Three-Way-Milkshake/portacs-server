package it.unipd.threewaymilkshake.portacs.server.engine;

public class Position extends AbstractLocation{
  public Position(int x, int y) {
    super(x, y);
    //TODO Auto-generated constructor stub
  }

  private Orientation orientation;
  
  public Orientation getOrientation() {
    return orientation;
  }

  public void setPosition(int x, int y, Orientation o){
    setX(x);
    setY(y);
    orientation=o;
  }
}