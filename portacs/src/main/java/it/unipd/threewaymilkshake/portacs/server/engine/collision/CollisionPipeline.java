/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server.engine.collision;

public class CollisionPipeline<I, O> {

  private final Handler<I, O> currentHandler;

  public CollisionPipeline(Handler<I, O> currentHandler) {
    this.currentHandler = currentHandler;
  }

  public <K> CollisionPipeline<I, K> addHandler(Handler<O, K> newHandler) {
    return new CollisionPipeline<>(input -> newHandler.process(currentHandler.process(input)));
  }

  public O execute(I input) {
    return currentHandler.process(input);
  }
}
