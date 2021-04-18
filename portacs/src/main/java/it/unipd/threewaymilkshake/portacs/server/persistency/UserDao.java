package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.util.List;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;

public interface UserDao{
  void updateUsers(List<User> u);
  List<User> readUsers();
}