/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Rappresenta un utente generico di portacs */
public abstract class User extends Client implements Serializable {
  @Expose private String firstName;
  @Expose private String lastName;
  @Expose private String pwdHash;
  // @Expose(serialize = false, deserialize = false)
  @Autowired PasswordEncoder passwordEncoder;

  /**
   * @param id: identificativo dell'utente
   * @param firstName: nome dell'utente
   * @param lastName: cognome dell'utente
   * @param pwdHash: stringa hash rappresentante la password dell'utente
   */
  public User(String id, String firstName, String lastName, String pwdHash) {
    super(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.pwdHash = pwdHash;
  }

  User(
      String id,
      String firstName,
      String lastName,
      String pwdHash,
      PasswordEncoder passwordEncoder) {
    this(id, firstName, lastName, pwdHash);
    this.passwordEncoder = passwordEncoder;
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
