package com.devblok.kpc.tools;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ActivityTools {
    public static void fullscreenMode(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
