package it.unipd.threewaymilkshake.portacs.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private String lastMessage;

  public Connection(Socket socket, BufferedReader in, PrintWriter out) {
    this.socket = socket;
    this.in = in;
    this.out = out;
  }
  
  public String read(){
    try{
      lastMessage=in.readLine();
    }
    catch(IOException e){
      lastMessage=null;
      close();
    }

    return lastMessage;
  }

  public boolean writeToBuffer(String msg){
    out.print(msg);
    return !out.checkError();
  }

  public boolean send(String msg){
    out.println(msg);
    return !out.checkError();
  }

  public boolean sendBuffer(){
    out.println();
    return !out.checkError();
  }

  public boolean isAlive(){
    boolean alive=true;
    try{
      lastMessage=in.readLine();
    }
    catch(IOException e){
      alive=false;
      close();
    }

    return alive;
  }

  public String getLastMessage(){
    return lastMessage;
  }

  public void close(){
    try{
      in.close();
      out.close();
      socket.close();
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }
}