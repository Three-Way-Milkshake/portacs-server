package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import com.google.gson.annotations.Expose;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class User extends Client{
  @Expose private String firstName;
  @Expose private String lastName;
  @Expose private String pwdHash;
  // @Expose(serialize = false, deserialize = false)
  @Autowired
  PasswordEncoder passwordEncoder;

  public User(String id, String firstName, String lastName, String pwdHash) {
    super(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.pwdHash = pwdHash;
  }

  User(String id, String firstName, String lastName, String pwdHash, PasswordEncoder passwordEncoder){
    this(id, firstName, lastName, pwdHash);
    this.passwordEncoder=passwordEncoder;
  }

  @Override
  public boolean authenticate(String s) {
    return passwordEncoder.matches(s, pwdHash);
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