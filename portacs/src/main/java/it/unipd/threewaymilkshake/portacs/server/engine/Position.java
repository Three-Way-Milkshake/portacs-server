/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine;

public class Position extends AbstractLocation {
  private Orientation orientation;

  public Position(int x, int y, Orientation o) {
    super(x, y);
    orientation = o;
  }

  public Position(Position p) {
    super(p);
    orientation = ((Position) p).orientation;
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public void setPosition(int x, int y, Orientation o) {
    setX(x);
    setY(y);
    orientation = o;
  }

  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append(x);
    b.append(',');
    b.append(y);
    b.append(',');
    b.append(orientation.ordinal());

    return b.toString();
  }

  public Move transition(int xn, int yn) {
    Move r;
    if (xn == x && yn == y) r = Move.STOP;
    else if (xn < x) {
      // up
      r = moveUp();
      orientation = Orientation.UP;
    } else if (xn > x) {
      // down
      r = moveDown();
      orientation = Orientation.DOWN;
    } else if (yn < y) {
      // left
      r = moveLeft();
      orientation = Orientation.LEFT;
    } else {
      // right
      r = moveRight();
      orientation = Orientation.RIGHT;
    }

    return r;
  }

  private Move moveRight() {
    return switch (orientation) {
      case UP -> Move.TURNRIGHT;
      case DOWN -> Move.TURNLEFT;
      case LEFT -> Move.TURNAROUND;
      case RIGHT -> {
        ++y;
        yield Move.GOSTRAIGHT;
      }
    };
  }

  private Move moveLeft() {
    return switch (orientation) {
      case UP -> Move.TURNLEFT;
      case DOWN -> Move.TURNRIGHT;
      case LEFT -> {
        --y;
        yield Move.GOSTRAIGHT;
      }
      case RIGHT -> Move.TURNAROUND;
    };
  }

  private Move moveDown() {
    return switch (orientation) {
      case UP -> Move.TURNAROUND;
      case DOWN -> {
        ++x;
        yield Move.GOSTRAIGHT;
      }
      case LEFT -> Move.TURNLEFT;
      case RIGHT -> Move.TURNRIGHT;
    };
  }

  private Move moveUp() {
    return switch (orientation) {
      case UP -> {
        --x;
        yield Move.GOSTRAIGHT;
      }
      case DOWN -> Move.TURNAROUND;
      case LEFT -> Move.TURNRIGHT;
      case RIGHT -> Move.TURNLEFT;
    };
  }

  private void computeStraight() {
    switch (orientation) {
      case UP -> this.x--;
      case RIGHT -> this.y++;
      case DOWN -> this.x++;
      case LEFT -> this.y--;
    }
  }

  private void computeLeft() {
    switch (orientation) {
      case UP -> this.orientation = Orientation.LEFT;
      case RIGHT -> this.orientation = Orientation.UP;
      case DOWN -> this.orientation = Orientation.RIGHT;
      case LEFT -> this.orientation = Orientation.DOWN;
    }
  }

  private void computeRight() {
    switch (orientation) {
      case UP -> this.orientation = Orientation.RIGHT;
      case RIGHT -> this.orientation = Orientation.DOWN;
      case DOWN -> this.orientation = Orientation.LEFT;
      case LEFT -> this.orientation = Orientation.UP;
    }
  }

  private void computeTurnAround() {
    switch (orientation) {
      case UP -> this.orientation = Orientation.DOWN;
      case RIGHT -> this.orientation = Orientation.LEFT;
      case DOWN -> this.orientation = Orientation.UP;
      case LEFT -> this.orientation = Orientation.RIGHT;
    }
  }

  public Position computeNextPosition(Move move) {
    // System.out.println("( Position:" + this.getX() + " " + this.getY() + " " +
    // this.getOrientation());
    // System.out.println("Move:" + move + ")");
    switch (move) {
      case GOSTRAIGHT -> computeStraight();

      case TURNLEFT -> computeLeft();

      case TURNRIGHT -> computeRight();

      case TURNAROUND -> computeTurnAround();

      case STOP -> {
      }
      default -> throw new IllegalArgumentException("Unexpected value: " + move);
    }
    ;
    return this;
  }

  public SimplePoint getPoint() { // TODO:
    return new SimplePoint(x, y);
  }
}
