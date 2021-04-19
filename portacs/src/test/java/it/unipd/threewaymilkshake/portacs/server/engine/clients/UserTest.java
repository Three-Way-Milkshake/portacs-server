package it.unipd.threewaymilkshake.portacs.server.engine.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
// import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.connection.Connection;

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
  public void testAuthenticationAndConnectionBinding(){
    //TODO test auth
    //UsersList listMock=mock(UsersList.class);
    Connection connMock=mock(Connection.class);
    String pwdPlain="mario1";
    String pwdHash=passwordEncoder.encode(pwdPlain);
    String[] userData=new String[]{"1", "Mario", "Rossi"};
    User u=new Manager(userData[0], userData[1], userData[2], pwdHash, passwordEncoder);
    when(connMock.read()).thenReturn(userData[0])
      .thenReturn(pwdPlain);
    
    assertEquals(userData[0], connMock.read());
    assertFalse(u.isActive());
    if(u.authenticate(connMock.read())) u.bindConnection(connMock);
    assertTrue(u.isActive());
    u.clearConnection();
    assertFalse(u.isActive());

    assertEquals(userData[1], u.getFirstName());
    assertEquals(userData[2], u.getLastName());
  }

  @Test
  public void testMockMultipleCalls(){
    Connection connMock=mock(Connection.class);
    when(connMock.read()).thenReturn("first")
      .thenReturn("second")
      .thenReturn("third")
      .thenReturn(null);
    assertEquals("first", connMock.read());
    assertEquals("second", connMock.read());
    assertEquals("third", connMock.read());
    assertNull(connMock.read());
  }
}
