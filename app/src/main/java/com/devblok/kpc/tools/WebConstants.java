package com.devblok.kpc.tools;

import okhttp3.MediaType;

public class WebConstants {
    public static final String backendUrl = "http://kpc-app.ru/api";
    public static final String backendUrlBase = "http://kpc-app.ru";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "USER_ID_KEY";
}
