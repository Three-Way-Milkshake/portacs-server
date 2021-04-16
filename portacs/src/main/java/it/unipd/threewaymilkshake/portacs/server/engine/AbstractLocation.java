package it.unipd.threewaymilkshake.portacs.server.engine;

public abstract class AbstractLocation{
  protected int x;
  protected int y;
  public int getY() {
    return y;
  }
  public int getX() {
    return x;
  }
  public void setX(int x) {
    this.x = x;
  }
  public void setY(int y) {
    this.y = y;
  }
  public AbstractLocation(int x, int y) {
    this.x = x;
    this.y = y;
  }
  public AbstractLocation(AbstractLocation other){
    this.x=other.x;
    this.y=other.y;
  }
}