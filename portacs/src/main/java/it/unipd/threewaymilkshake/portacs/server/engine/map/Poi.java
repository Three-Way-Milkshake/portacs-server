/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.map;

import com.google.gson.annotations.Expose;
import it.unipd.threewaymilkshake.portacs.server.engine.AbstractLocation;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;

public class Poi {
  @Expose long id;
  @Expose String name;
  @Expose SimplePoint location; // TODO: capire se è corretto, prima era AbstractLocation
  @Expose PoiType type;

  public Poi(long id, String name, SimplePoint location, PoiType type) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.type = type;
  }

  public Poi(long id, String name, SimplePoint location) { // TODO: rimuovere
    this.id = id;
    this.name = name;
    this.location = location;
    this.type = PoiType.LOAD;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((location == null) ? 0 : location.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Poi other = (Poi) obj;
    if (id != other.id) return false;
    if (location == null) {
      if (other.location != null) return false;
    } else if (!location.equals(other.location)) return false;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return true;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) { // TODO: serve?
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) { // TODO: serve?
    this.name = name;
  }

  public AbstractLocation getLocation() {
    return location;
  }

  public void setLocation(SimplePoint location) {
    this.location = location;
  }

  public PoiType getType() {
    return type;
  }

  public void setType(PoiType type) {
    this.type = type;
  }
}
