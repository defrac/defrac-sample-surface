package com.defrac.sample.surface;

import defrac.display.GLSurface;
import defrac.ui.*;

/**
 *
 */
public class SurfaceScreen extends Screen {
  DisplayList displayList;

  @Override
  protected void onCreate() {
    super.onCreate();

    displayList = new DisplayList();

    displayList.onStageReady(stage -> {
      // Create a GLSurface element with its own renderer implementation
      // We pass the UIEventManager instance to the SurfaceRenderer so
      // we can easily access global pointer coordinates without having
      // to listen for events
      GLSurface surface =
          new GLSurface(width(), height(), new SurfaceRenderer(stage.eventManager()));

      stage.addChild(surface);

      // Adjusts the quality of the shader:
      //
      //   1 = No downsampling
      //   4 = OK
      //   8 = Starts to look really ugly
      int ratio = 4;

      surface.size(stage.width() / ratio, stage.height() / ratio);
      surface.scaleTo(ratio);

      stage.globalEvents().onResize.add(event -> {
        // We do not want to fry the graphics card so we simply
        // draw a smaller surface and scale the result
        surface.size(event.width / ratio, event.height / ratio);
        surface.scaleTo(ratio);
      });
    });

    rootView(displayList);
  }

  @Override
  protected void onPause() {
    super.onPause();
    displayList.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    displayList.onResume();
  }
}
