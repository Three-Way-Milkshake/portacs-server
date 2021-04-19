package it.unipd.threewaymilkshake.portacs.server.engine.clients;

public class Admin extends User{

  public Admin(String id, String firstName, String lastName, String pwdHash) {
    super(id, firstName, lastName, pwdHash);
  }

  @Override
  public void processCommunication() {
    // TODO Auto-generated method stub
    
  }

}