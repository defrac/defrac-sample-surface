package com.defrac.sample.surface;

import defrac.app.Bootstrap;
import defrac.app.GenericApp;
import defrac.display.GLSurface;

/**
 *
 */
public class SurfaceSample extends GenericApp {
  public static void main(String[] args) {
    Bootstrap.run(new SurfaceSample());
  }

  GLSurface surface;

  @Override
  protected void onCreate() {
    // Create a GLSurface element with its own renderer implementation
    // We pass the UIEventManager instance to the SurfaceRenderer so
    // we can easily access global pointer coordinates without having
    // to listen for events
    surface = new GLSurface(width(), height(), new SurfaceRenderer(stage().eventManager()));

    addChild(surface);
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
    // draw a smaller surface and scale the result
    surface.size(width / ratio, height / ratio);
    surface.scaleTo(ratio);
  }
}
