/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ConnectionAccepterTest {
  @Test
  public void testCreationAndStart() {
    ConnectionHandler handler = mock(ConnectionHandler.class);
    ConnectionAccepter acc = new ConnectionAccepter(handler);
    assertNotNull(acc);
  }
}
