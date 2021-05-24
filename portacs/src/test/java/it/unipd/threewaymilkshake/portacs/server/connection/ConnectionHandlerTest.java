/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ConnectionHandlerTest {
  @Test
  public void testEnums() {
    assertNotNull(ClientType.FORKLIFT);
    assertNotNull(ClientType.USER);
  }

  @Test
  public void testCreationAndExecution() throws IOException, InterruptedException {
    UsersList u = mock(UsersList.class);
    ForkliftsList f = mock(ForkliftsList.class);
    ConnectionHandler handler = new ConnectionHandler(u, f);
    assertNotNull(handler);
    Socket s1 = mock(Socket.class), s2 = mock(Socket.class), s3 = mock(Socket.class);
    // InputStream in1=mock(InputStream.class), in2=mock(InputStream.class);
    InputStream in1 = new ByteArrayInputStream("FORKLIFT".getBytes()),
        in2 = new ByteArrayInputStream("USER".getBytes()),
        in3 = new ByteArrayInputStream("Something wrong".getBytes());
    OutputStream out1 = mock(OutputStream.class), out3 = mock(OutputStream.class);

    when(s1.getInputStream()).thenReturn(in1);
    when(s1.getOutputStream()).thenReturn(out1);
    when(s2.getInputStream()).thenReturn(in2);
    when(s2.getOutputStream()).thenReturn(out1);
    when(s3.getInputStream()).thenReturn(in3);
    when(s3.getOutputStream()).thenReturn(out3);

    handler.addToBuffer(s1);
    handler.addToBuffer(s2);
    handler.addToBuffer(s3);
    handler.execute();

    verify(f).auth(any());
    verify(u).auth(any());
  }
}
