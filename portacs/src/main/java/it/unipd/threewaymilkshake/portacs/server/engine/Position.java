package it.unipd.threewaymilkshake.portacs.server.engine;

public class Position extends AbstractLocation{
  private Orientation orientation;
  
  public Position(int x, int y, Orientation o) {
    super(x, y);
    orientation=o;
  }

  public Position(Position p){
    super(p);
    orientation=((Position)p).orientation;
  }
  
  public Orientation getOrientation() {
    return orientation;
  }

  public void setPosition(int x, int y, Orientation o){
    setX(x);
    setY(y);
    orientation=o;
  }

  public Move transition(int xn, int yn){
    Move r;

    if(xn<x){
      //up
      r=switch(orientation){
        case UP -> {
          --x;
          yield Move.GOSTRAIGHT;
        }
        case DOWN -> Move.TURNAROUND;
        case LEFT -> Move.TURNRIGHT;
        case RIGHT -> Move.TURNLEFT;
      };
      orientation=Orientation.UP;
    }
    else if(xn>x){
      //down
      r=switch(orientation){
        case UP -> Move.TURNAROUND;
        case DOWN -> {
          ++x;
          yield Move.GOSTRAIGHT;
        }
        case LEFT -> Move.TURNLEFT;
        case RIGHT -> Move.TURNRIGHT;
      };
      orientation=Orientation.DOWN;
    }
    else if(yn<y){
      //left
      r=switch(orientation){
        case UP -> Move.TURNLEFT;
        case DOWN -> Move.TURNRIGHT;
        case LEFT -> {
          --y;
          yield Move.GOSTRAIGHT;
        }
        case RIGHT -> Move.TURNAROUND;
      };
      orientation=Orientation.LEFT;
    }
    else{
      //right
      r=switch(orientation){
        case UP -> Move.TURNRIGHT;
        case DOWN -> Move.TURNLEFT;
        case LEFT -> Move.TURNAROUND;
        case RIGHT -> {
          ++y;
          yield Move.GOSTRAIGHT;
        }
      };
      orientation=Orientation.RIGHT;
    }

    return r;
  }
}