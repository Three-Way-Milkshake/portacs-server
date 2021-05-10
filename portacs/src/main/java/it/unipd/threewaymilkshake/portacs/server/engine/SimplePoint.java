/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

public class SimplePoint extends AbstractLocation {
  @Override
  public boolean equals(Object obj) {
    // TODO Auto-generated method stub
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    // TODO Auto-generated method stub
    return super.hashCode();
  }

  public SimplePoint(int x, int y) {
    super(x, y);
  }

  public int calculateDistance(SimplePoint destination) {
    if (this.x == destination.x) return Math.abs(this.y - destination.y);
    else if (this.y == destination.y) return Math.abs(this.x - destination.x);
    else return 0;
  }
}
