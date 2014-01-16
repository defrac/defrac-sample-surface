package com.defrac.sample.canvas;

import defrac.app.Bootstrap;
import defrac.app.GenericApp;
import defrac.display.Canvas;
import defrac.event.StageEvent;
import defrac.lang.Procedure;

/**
 *
 */
public class CanvasSample extends GenericApp {
  public static void main(String[] args) {
    Bootstrap.run(new CanvasSample());
  }
  @Override
  protected void onCreate() {
    // Create a Canvas element with its own renderer implementation
    // We pass the UIEventManager instance to the CanvasRenderer so
    // we can easily access global pointer coordinates without having
    // to listen for events
    final Canvas canvas = new Canvas(width(), height(), new CanvasRenderer(stage().eventManager()));

    // Add the canvas to the stage
    addChild(canvas);

    // Make sure we are always filling the entire screen
    onResize().attach(new Procedure<StageEvent.Resize>() {
      @Override
      public void apply(StageEvent.Resize resize) {
        canvas.size(width(), height());
      }
    });
  }
}
