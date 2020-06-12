package com.example.vehicledriver.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalPref {
    private final static String SERVER_KEY = "serverkey";
    public final static String SERVER_KEY_VAL = "AAAAHa48jJk:APA91bHD4qvEh8xe0hPT9-x1avqoQUFauVR1ljMZHxeeoXPf4Fmcqa2Wnsykb8JqxRdvJfPpBNBhZddWiG6hwnbBRtDEq1ZsYHDVC7izDW2tzJi_ksBQ5RSdBmYVmaucX7A22Y4ivzCe";
    public final static String TOKEN_VAL = "fH9x6fBKTdO34EJXLofDNI:APA91bHNWHC2kaEMHrWMqyMBYGRmqYrx8vRi7XNzfDySj1PxNO2fg8zegGSQklQjqplYznqegtQ8FqUhnUNq6vHld-muFEE7ks_1Tv4ulEgoWGIrbVzpcGIzxkt8KoGQHaPwbssnb6S6";
    private final static String TOKEN = "token";
    private final static String PREF = "vehicle_track";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    GlobalPref() {
    }

    public GlobalPref(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setServerKey(String s) {
        editor.putString(SERVER_KEY, s);
        editor.apply();
    }

    public String getServerKey() {
        return sharedPreferences.getString(SERVER_KEY,null);
    }

    public void setToken(String s) {
        editor.putString(TOKEN, s);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN,null);
    }


}
