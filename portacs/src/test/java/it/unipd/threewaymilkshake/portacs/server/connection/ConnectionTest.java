package it.unipd.threewaymilkshake.portacs.server.connection;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

import org.junit.jupiter.api.Test;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ConnectionTest {
  @Test
  public void testCreateConnection() throws IOException{
    Socket s=mock(Socket.class);
    BufferedReader in=mock(BufferedReader.class);
    PrintWriter out=mock(PrintWriter.class);
    String r="reading test";
    when(in.readLine()).thenReturn(r);

    Connection c=new Connection(s, in, out);
    assertTrue(c.isAlive());
    assertEquals(c.getLastMessage(), r);
    assertTrue(c.send(r));
    verify(in, times(1)).readLine();
    verify(out, times(1)).println(anyString());
  }
}
