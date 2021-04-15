package it.unipd.threewaymilkshake.portacs.server.connection;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.net.InetAddress;

class ConnectionAccepter implements Runnable{
  private ConnectionHandler handler;
  private ServerSocket ssocket;
  private final static int PORT=1723;
  private InetAddress addr;

  ConnectionAccepter(ConnectionHandler connectionHandler){
    this.handler=connectionHandler;
    try{
      addr=InetAddress.getByName("0.0.0.0");
      ssocket=new ServerSocket(PORT, 0, addr);
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    Socket s;
    while(true){
      try{
        s=ssocket.accept();
        handler.addToBuffer(s);
        handler.notify();
      } 
      catch(IOException e){
        e.printStackTrace();
      }
    }
  }

  
}