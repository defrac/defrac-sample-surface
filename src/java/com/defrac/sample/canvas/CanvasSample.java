package com.defrac.sample.canvas;

import defrac.app.Bootstrap;
import defrac.app.GenericApp;
import defrac.display.Canvas;

/**
 *
 */
public class CanvasSample extends GenericApp {
  public static void main(String[] args) {
    Bootstrap.run(new CanvasSample());
  }

  Canvas canvas;

  @Override
  protected void onCreate() {
    // Create a Canvas element with its own renderer implementation
    // We pass the UIEventManager instance to the CanvasRenderer so
    // we can easily access global pointer coordinates without having
    // to listen for events
    canvas = new Canvas(width(), height(), new CanvasRenderer(stage().eventManager()));

    addChild(canvas);
  }

  @Override
  protected void onResize(float width, float height) {
    // Adjusts the quality of the shader:
    //
    //   1 = No downsampling
    //   4 = OK
    //   8 = Starts to look really ugly
    int ratio = 4;

    // We do not want to fry the graphics card so we simply
    // draw a smaller canvas and scale the result
    canvas.size(width/ratio, height/ratio);
    canvas.scaleTo(ratio);
  }
}
