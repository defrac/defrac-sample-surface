package com.defrac.sample.surface;

import defrac.ui.FrameBuilder;

/**
 *
 */
public final class Main {
  public static void main(String[] args) {
    FrameBuilder.
        forScreen(new SurfaceScreen()).
        show();
  }
}
