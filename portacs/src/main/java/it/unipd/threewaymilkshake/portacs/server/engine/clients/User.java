/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Arrays;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Rappresenta un utente generico di portacs */
public abstract class User extends Client implements Serializable {
  @Expose private String firstName;
  @Expose private String lastName;
  @Expose private String pwdHash;
  // @Expose(serialize = false, deserialize = false)
  PasswordEncoder passwordEncoder;

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

  void setPasswordEncoder(PasswordEncoder passwordEncoder) {
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

  public void setPwdHash(String pwdHash) {
    this.pwdHash = pwdHash;
  }

  private void editProfile(String what, String value) {
    switch (what) {
      case "NAME":
        this.firstName = value;
        break;
      case "LAST":
        this.lastName = value;
      case "PWD":
        this.pwdHash = passwordEncoder.encode(value);
    }
  }

  @Override
  public void processCommunication() {
    if (active && connection.isAliveFlush()) {
      try {
        String[] commands =
            connection.getLastMessage().split(";"); // TODO avoid null pointer exception
        Arrays.stream(commands)
            .forEach(
                c -> {
                  String[] par = c.split(",");

                  /* System.out.print("(user) " + id + ") Command: " + par[0] + ", params: ");
                  for (int i = 1; i < par.length; ++i) {
                    System.out.print(par[i] + " ");
                  }
                  System.out.println(); */

                  switch (par[0]) {
                    case "LOGOUT":
                      clearConnection();
                      break;
                    case "EDIT":
                      editProfile(par[1], par[2]);
                    default:
                      // System.out.println("Unrecognized message: " + par[0]);
                      // not a common operation
                  }
                });
      } catch (NullPointerException e) {
        // interruzione inaspettate della connessione
        clearConnection();
      }
    } else {
      clearConnection();
    }
  }

  abstract String getRole();
}
