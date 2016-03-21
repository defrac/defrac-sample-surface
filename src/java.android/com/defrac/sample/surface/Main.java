package com.defrac.sample.surface;

import android.content.Intent;
import android.os.Build;
import defrac.dni.Activity;
import defrac.dni.IntentFilter;
import defrac.ui.Screen;
import defrac.ui.ScreenActivity;

import javax.annotation.Nonnull;

/**
 *
 */
@Activity(label = "Surface Sample", filter = @IntentFilter(action = Intent.ACTION_MAIN, category = Intent.CATEGORY_LAUNCHER))
public final class Main extends ScreenActivity {
  @Nonnull
  @Override
  protected Screen createScreen() {
    return new SurfaceScreen();
  }
}
