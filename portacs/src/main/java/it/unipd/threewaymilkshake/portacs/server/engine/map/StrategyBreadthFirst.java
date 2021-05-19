/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.map;

import it.unipd.threewaymilkshake.portacs.server.engine.AbstractLocation;
import it.unipd.threewaymilkshake.portacs.server.engine.Move;
import it.unipd.threewaymilkshake.portacs.server.engine.Position;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StrategyBreadthFirst implements PathFindingStrategy {

  private int[][] nodes;

  @Override
  public List<Move> getPath(int[][] map, AbstractLocation start, AbstractLocation end) {
    nodes = map;
    int bakStart = map[start.getX()][start.getY()];
    int bakEnd = map[end.getX()][end.getY()];
    map[start.getX()][start.getY()] = 9;
    map[end.getX()][end.getY()] = 10;

    List<Node> path = shortestPath();
    // checking to reverse path shouldn't be needed since it should always generate start to end
    List<AbstractLocation> pathPoints =
        path.stream().map(n -> new SimplePoint(n.x, n.y)).collect(Collectors.toList());

    Position iterator = new Position((Position) start);
    List<Move> moves = new LinkedList<>();

    path.remove(0);
    path.stream()
        .forEach(
            p -> {
              Move tmp;
              do {
                tmp = iterator.transition(p.x, p.y);
                moves.add(tmp);
              } while (tmp != Move.GOSTRAIGHT && tmp != Move.STOP);
            });

    // restore cells in map
    map[start.getX()][start.getY()] = bakStart;
    map[end.getX()][end.getY()] = bakEnd;
    return moves;
  }

  public List<Node> shortestPath() {

    Map<Node, Node> parents = new HashMap<Node, Node>();
    Node start = null;
    Node end = null;

    start = processNodes();

    if (start == null) {
      throw new RuntimeException("can't find start node");
    }

    List<Node> tmp = new ArrayList<Node>(); // lista usata per scorrere i nodi percorribili
    tmp.add(start);
    parents.put(start, null);

    boolean reachDestination = false;
    while (tmp.size() > 0 && !reachDestination) {
      Node currentNode = tmp.remove(0);
      List<Node> children = getChildren(currentNode);
      loop:
      for (Node child : children) {

        if (!parents.containsKey(
            child)) { // se una cella non è ancora stata visitata viene aggiunta
          parents.put(child, currentNode);

          int value = child.getValue();

          /** 0: ostacolo 1: percorribile in tutti i sensi 2: UP 3: RIGHT 4: DOWN 5: LEFT */
          switch (value) {
            case 1:
              tmp.add(child);
              break;
            case 2:
              if (currentNode.getX() == child.getX() + 1) tmp.add(child);
              break;
            case 3:
              if (currentNode.getY() == child.getY() - 1) tmp.add(child);
              break;
            case 4:
              if (currentNode.getX() == child.getX() - 1) tmp.add(child);
              break;
            case 5:
              if (currentNode.getY() == child.getY() + 1) tmp.add(child);
              break;
            case 10:
              {
                tmp.add(child);
                reachDestination = true;
                end = child;
                break loop;
              }
          }

          /* if (value == 1) {
            tmp.add(child);
          } else if (value == 9) {
            tmp.add(child);
            reachDestination = true;
            end = child;
            break;
          } */
        }
      }
    }

    if (end == null) {
      throw new RuntimeException("can't find end node");
    }

    Node node = end;
    List<Node> path = new ArrayList<Node>();
    while (node != null) {
      path.add(0, node);
      node = parents.get(node);
    }
    printPath(path);
    return path;
  }

  private Node processNodes() {
    Node start = null;
    for (int row = 0; row < nodes.length; row++) {
      for (int column = 0; column < nodes[row].length; column++) {
        if (nodes[row][column] == 9) {
          start = new Node(row, column, nodes[row][column]);
          break; // might add for condition to avoid
        }
      }
      if (start != null) {
        break; // might add for condition to avoid
      }
    }
    return start;
  }

  private List<Node> getChildren(Node parent) {
    List<Node> children = new ArrayList<Node>();
    int x = parent.getX();
    int y = parent.getY();
    if (x - 1 >= 0) {
      Node child = new Node(x - 1, y, nodes[x - 1][y]);
      children.add(child);
    }
    if (y - 1 >= 0) {
      Node child = new Node(x, y - 1, nodes[x][y - 1]);
      children.add(child);
    }
    if (x + 1 < nodes.length) {
      Node child = new Node(x + 1, y, nodes[x + 1][y]);
      children.add(child);
    }
    if (y + 1 < nodes[0].length) {
      Node child = new Node(x, y + 1, nodes[x][y + 1]);
      children.add(child);
    }
    return children;
  }

  private void printPath(List<Node> path) {
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";

    for (int row = 0; row < nodes.length; row++) {
      for (int column = 0; column < nodes[row].length; column++) {
        String value = nodes[row][column] + "";

        for (int i = 1; i < path.size() - 1; i++) {
          Node node = path.get(i);
          if (node.getX() == row && node.getY() == column) {
            value = ANSI_RED + "X" + ANSI_RESET;
            break;
          }
        }
        if (column == nodes[row].length - 1) {
          System.out.println(value + " ");
        } else {
          System.out.print(value + " ");
        }
      }

      /* if (row < nodes.length - 1) {
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < nodes[row].length - 1; j++) {
            System.out.print(".     ");
          }
          System.out.println(".     ");
        }
      } */
    }
    System.out.println();
    System.out.println("Path: " + path);
  }

  private class Node {
    private int x;
    private int y;
    private int value;

    private Node(int x, int y, int value) {
      this.x = x;
      this.y = y;
      this.value = value;
    }

    private int getX() {
      return x;
    }

    private int getY() {
      return y;
    }

    private int getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "(x: " + x + " y: " + y + ")";
    }

    @Override
    public int hashCode() {
      return x * y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      if (this.getClass() != o.getClass()) return false;
      Node node = (Node) o;
      return x == node.x && y == node.y;
    }
  }
}
