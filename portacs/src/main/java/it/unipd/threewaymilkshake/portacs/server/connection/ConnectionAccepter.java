/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class ConnectionAccepter /* implements Runnable */ {
  // private final static String CERT_FILE=System.getProperty("user.dir")+"/trust.jks";

  private ConnectionHandler handler;
  private ServerSocket ssocket;
  private static final int PORT = 1723;
  private InetAddress addr;

  ConnectionAccepter(ConnectionHandler connectionHandler) {
    this.handler = connectionHandler;
    try {
      /* System.setProperty("jdk.tls.server.protocols", "TLSv1.2");
      System.setProperty("javax.net.ssl.keyStore", CERT_FILE);
      System.out.println(CERT_FILE);
      System.setProperty("javax.net.ssl.keyStorePassword", "portacs"); */

      addr = InetAddress.getByName("0.0.0.0");
      ssocket = new ServerSocket(PORT, 0, addr);
      // ssocket=SSLServerSocketFactory.getDefault().createServerSocket(PORT, 0, addr);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // @Override
  @Scheduled(fixedDelay = 1000, initialDelay = 500)
  public void run() {
    System.out.println("ACCEPTER started");
    Socket s;
    while (true) {
      try {
        s = ssocket.accept();
        handler.addToBuffer(s);
        // handler.notify();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
