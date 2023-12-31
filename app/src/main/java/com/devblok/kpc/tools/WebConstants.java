package com.devblok.kpc.tools;

import okhttp3.MediaType;

public class WebConstants {
    public static final String backendUrl = "http://10.0.2.2:8080/api";
    public static final String backendUrlBase = "http://10.0.2.2:8080";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "USER_ID_KEY";
}
