package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableAutoConfiguration
public class UserTest {
  //@Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  public UserTest(PasswordEncoder passwordEncoder){
    this.passwordEncoder=passwordEncoder;
  }


  @Test
  public void testEncoder(){
    //PasswordEncoder passwordEncoder2=new BCryptPasswordEncoder();;
    String pwd="caesar";
    String enc=passwordEncoder.encode(pwd);
    assertTrue(passwordEncoder.matches(pwd, enc));
    assertFalse(passwordEncoder.matches(pwd+"4", enc));
  }

  @Test
  public void testAuthentication(){
    //TODO test auth
    //UsersList listMock=Mockito.mock(UsersList.class);
  }
}
