/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import java.util.Random;

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

  public SimplePoint generateNearRandomPoint() {
    Random rand = new Random();
    int random = rand.nextInt(4);
    SimplePoint toReturn;
    if (random == 0) {
      toReturn = new SimplePoint(this.getX() + 1, this.getY());
    } else if (random == 1) {
      toReturn = new SimplePoint(this.getX(), this.getY() + 1);
    } else if (random == 2) {
      toReturn = new SimplePoint(this.getX() - 1, this.getY());
    } else {
      toReturn = new SimplePoint(this.getX(), this.getY() - 1);
    }
    return toReturn;
  }
}
