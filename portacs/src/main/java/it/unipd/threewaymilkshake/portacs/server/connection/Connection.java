/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private String lastMessage;
  private String nextMessageToSend;

  public Connection(Socket socket, BufferedReader in, PrintWriter out) {
    this.socket = socket;
    this.in = in;
    this.out = out;
  }

  public String read() {
    try {
      lastMessage = in.readLine();
    } catch (IOException e) {
      lastMessage = null;
      close();
    }

    return lastMessage;
  }

  public boolean writeToBuffer(String msg) {
    // out.print(msg);
    if (nextMessageToSend == null) {
      nextMessageToSend = msg;
    } else {
      nextMessageToSend += msg;
    }
    return !out.checkError();
  }

  public boolean send(String msg) {
    writeToBuffer(msg);
    out.println(nextMessageToSend);
    nextMessageToSend = null;
    return !out.checkError();
  }

  public boolean sendBuffer() {
    out.println();
    return !out.checkError();
  }

  /**
   * Tries to read a message to check wether connection is still alive If so, returns true and the
   * read message is saved in lastMessage
   *
   * @return true if alive
   */
  public boolean isAlive() {
    boolean alive = true;
    try {
      lastMessage = in.readLine();
      // lastMessage="";
      // while(in.ready())
      //   lastMessage+=in.readLine();
    } catch (IOException e) {
      alive = false;
      close();
    }

    return alive;
  }

  public boolean isAliveFlush() {
    boolean alive = true;
    try {
      // if(in.ready())lastMessage = in.lines().collect(Collectors.joining());
      // if(lastMessage==null) throw new IOException();

      // lastMessage=in.readLine();
      lastMessage = "";
      if (in.ready()) {
        while (in.ready()) {
          lastMessage += in.readLine();
        }
      } else {
        // alive=false; //TODO FIX
        // close();
      }

    } catch (IOException e) {
      alive = false;
      close();
    }

    return alive;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public void close() {
    try {
      in.close();
      out.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
