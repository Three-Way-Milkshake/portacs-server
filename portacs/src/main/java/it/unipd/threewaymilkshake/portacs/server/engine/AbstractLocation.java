/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

import com.google.gson.annotations.Expose;

public abstract class AbstractLocation {
  @Expose protected int x;
  @Expose protected int y;

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

  public AbstractLocation(AbstractLocation other) {
    this.x = other.x;
    this.y = other.y;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    AbstractLocation other = (AbstractLocation) obj;
    if (x != other.x) return false;
    if (y != other.y) return false;
    return true;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
