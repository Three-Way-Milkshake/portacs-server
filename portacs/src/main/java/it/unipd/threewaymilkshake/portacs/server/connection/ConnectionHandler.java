/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.connection;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConnectionHandler /* implements Runnable */ {

  private AbstractQueue<Socket> buffer = new ConcurrentLinkedQueue<>();
  private UsersList users;
  private ForkliftsList forklifts;
  private int testCounter;

  public ConnectionHandler(UsersList usersList, ForkliftsList forkliftsList) {
    this.users = usersList;
    this.forklifts = forkliftsList;
    this.testCounter = 1;
  }

  @Scheduled(fixedDelay = 1000, initialDelay = 1000)
  public void execute() throws InterruptedException, IOException {
    // while(true){
    System.out.println("Hello from handler with: " + (testCounter++));
    //   Thread.sleep(1000);
    // }
    /* if(!buffer.isEmpty()){
      System.out.println("REACHED");
      Socket s=buffer.poll();
      BufferedReader in =
        new BufferedReader(new InputStreamReader(s.getInputStream()));
      System.out.println(in.readLine());
    } */
    if (!buffer.isEmpty()) {
      buffer.stream()
          // .parallel()
          .forEach(
              s -> {
                try {
                  BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                  PrintWriter out = new PrintWriter(s.getOutputStream());
                  Connection c = new Connection(s, in, out);
                  switch (in.readLine()) {
                    case "FORKLIFT":
                      forklifts.auth(c);
                      break;
                    case "USER":
                      users.auth(c);
                      break;
                    default:
                      System.out.println("UNRECOGNIZED CLIENT;");
                      out.println("FAILED;unrecognized connection type;");
                      c.close();
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });

      buffer.clear();
    }
  }

  // @Override
  // @Scheduled(fixedDelay = 100000, initialDelay = 500)
  /* public void run() {
    while (true) {
      try {
        System.out.println("Handler started");
        while (!buffer.isEmpty()) {
          buffer.stream()
              .parallel()
              .forEach(
                  s -> {
                    try {
                      BufferedReader in =
                          new BufferedReader(new InputStreamReader(s.getInputStream()));
                      PrintWriter out = new PrintWriter(s.getOutputStream());
                      Connection c = new Connection(s, in, out);
                      switch (in.readLine()) {
                        case "FORKLIFT":
                          forklifts.auth(c);
                          break;
                        case "USER":
                          users.auth(c);
                          break;
                        default:
                          out.println("FAILED;unrecognized connection type");
                          c.close();
                      }
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  });
        }
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  } */

  public void addToBuffer(Socket s) {
    buffer.add(s);
  }
}

enum ClientType {
  FORKLIFT,
  USER
}
