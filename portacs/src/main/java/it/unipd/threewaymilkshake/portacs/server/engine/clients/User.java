package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class User extends Client{
  private String firstName;
  private String lastName;
  private String pwdHash;
  @Autowired PasswordEncoder pwdEncoder;

  public User(String id, String firstName, String lastName, String pwdHash) {
    super(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.pwdHash = pwdHash;
  }

  @Override
  public boolean authenticate(String s) {
    return pwdEncoder.matches(s, pwdHash);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  
}