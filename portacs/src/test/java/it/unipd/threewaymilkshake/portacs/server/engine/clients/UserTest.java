package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class UserTest {
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void testEncoder(){
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
