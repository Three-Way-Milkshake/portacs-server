/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.persistency;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;
import java.util.List;

public interface ForkliftDao {
  // void addForklift(Forklift f);
  void updateForklifts(List<Forklift> f);

  List<Forklift> readForklifts();
}
