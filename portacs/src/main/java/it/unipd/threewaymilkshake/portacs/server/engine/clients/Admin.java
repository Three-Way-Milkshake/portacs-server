package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Admin extends User{

  public Admin(String id, String firstName, String lastName, String pwdHash) {
    super(id, firstName, lastName, pwdHash);
  }

  public Admin(String id, String firstName, String lastName, String pwdHash, PasswordEncoder passwordEncoder) {
    super(id, firstName, lastName, pwdHash, passwordEncoder);
  }

  @Override
  public void processCommunication() {
    // TODO Auto-generated method stub
    
  }

}