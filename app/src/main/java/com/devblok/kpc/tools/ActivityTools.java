package com.devblok.kpc.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import okhttp3.OkHttpClient;

public class ActivityTools {
    public static void fullscreenMode(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void setupBackLastFragment(Intent intent, int menu) {
        Bundle bundleBack = new Bundle();
        bundleBack.putInt("lastFragment", menu);
        intent.putExtras(bundleBack);
    }

    public static void closeAllConnections() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.connectionPool().evictAll();
    }
}
