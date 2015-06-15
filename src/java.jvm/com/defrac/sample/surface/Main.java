package com.defrac.sample.surface;

import defrac.ui.FrameBuilder;

/**
 *
 */
public final class Main {
  public static void main(String[] args) {
    FrameBuilder.
        forScreen(new SurfaceScreen()).
        title("Surface Sample").
        width(512).
        height(512).
        backgroundColor(0xff000000).
        resizable().
        show();
  }
}
