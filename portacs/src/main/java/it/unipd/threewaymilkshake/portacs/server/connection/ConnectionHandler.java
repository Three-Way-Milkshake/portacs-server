package it.unipd.threewaymilkshake.portacs.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

class ConnectionHandler implements Runnable{

  private AbstractQueue<Socket> buffer=new ConcurrentLinkedQueue<>();

  @Override
  public void run() {
    Socket s;
    BufferedReader in;
    PrintWriter out;
    while(true){
      try{
        while(!buffer.isEmpty()){
          s=buffer.poll();
          in=new BufferedReader(new InputStreamReader(s.getInputStream()));
          out=new PrintWriter(s.getOutputStream());
          wait();
        }
      }
      catch(InterruptedException e){
        e.printStackTrace();
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }    
  }

  public void addToBuffer(Socket s){
    buffer.add(s);
  }

}