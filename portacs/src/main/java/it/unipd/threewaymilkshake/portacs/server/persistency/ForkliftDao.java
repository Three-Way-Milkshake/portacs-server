package it.unipd.threewaymilkshake.portacs.server.persistency;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.Forklift;

interface ForkliftDao{
  void addForklift(Forklift f);
}