package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.util.List;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;

public interface ForkliftDao{
  //void addForklift(Forklift f);
  void updateForklifts(List<Forklift> f);
  
  List<Forklift> readForklifts();
}