package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Manager extends User{

  public Manager(String id, String firstName, String lastName, String pwdHash) {
    super(id, firstName, lastName, pwdHash);
  }

  Manager(String id, String firstName, String lastName, String pwdHash, PasswordEncoder passwordEncoder) {
    super(id, firstName, lastName, pwdHash, passwordEncoder);
  }

  @Override
  public void processCommunication() {
    // TODO Auto-generated method stub
    
  }

}