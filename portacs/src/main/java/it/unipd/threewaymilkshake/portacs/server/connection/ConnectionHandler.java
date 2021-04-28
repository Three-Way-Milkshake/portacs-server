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

public class ConnectionHandler implements Runnable {

  private AbstractQueue<Socket> buffer = new ConcurrentLinkedQueue<>();
  private UsersList users;
  private ForkliftsList forklifts;

  public ConnectionHandler(UsersList usersList, ForkliftsList forkliftsList) {
    this.users = usersList;
    this.forklifts = forkliftsList;
  }

  @Override
  public void run() {
    /* Socket s;
    BufferedReader in;
    PrintWriter out; */

    /* Callable<ClientType> task=()->{
      return ClientType.FORKLIFT;
    }; */

    while (true) {
      try {
        while (!buffer.isEmpty()) {
          // s=buffer.poll();
          /**
           * might stream pending connections and submit using task callable, getting results as
           * type and assing, it is a possible evolution --> buffer.stream()...
           */
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
  }

  public void addToBuffer(Socket s) {
    buffer.add(s);
  }
}

enum ClientType {
  FORKLIFT,
  USER
}
