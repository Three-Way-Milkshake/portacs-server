/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ConnectionTest {
  @Test
  public void testCreateConnection() throws IOException {
    Socket s = mock(Socket.class);
    BufferedReader in = mock(BufferedReader.class);
    PrintWriter out = mock(PrintWriter.class);
    String r = "reading test";
    when(in.readLine()).thenReturn(r);

    Connection c = new Connection(s, in, out);
    assertTrue(c.isAlive());
    assertEquals(c.getLastMessage(), r);
    assertTrue(c.send(r));
    verify(in, times(1)).readLine();
    verify(out, times(1)).println(anyString());
  }

  @Test
  public void testRead() throws IOException {
    Socket s = mock(Socket.class);
    BufferedReader in = mock(BufferedReader.class);
    BufferedReader in2 = mock(BufferedReader.class);
    PrintWriter out = mock(PrintWriter.class);
    String r = "reading test";
    when(in.readLine()).thenReturn(r);
    when(in2.readLine()).thenThrow(new IOException());
    Connection c = new Connection(s, in, out);
    Connection c2 = new Connection(s, in2, out);
    assertEquals(r, c.read());
    assertNull(c2.read());
    // assertThrows(IOException.class, ()->{c2.read();});
  }
}
