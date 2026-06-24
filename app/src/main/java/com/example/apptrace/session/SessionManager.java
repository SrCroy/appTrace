package com.example.apptrace.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.apptrace.model.auth.LoginData;

public class SessionManager {

    private static final String PREF_NAME = "apptrace_session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";

    private static SessionManager instance;
    private final SharedPreferences preferences;

    private SessionManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void saveSession(LoginData data) {
        preferences.edit()
                .putString(KEY_TOKEN, data.getToken())
                .putInt(KEY_USER_ID, data.getUser().getId())
                .putString(KEY_USER_NAME, data.getUser().getNombre())
                .putString(KEY_USER_EMAIL, data.getUser().getEmail())
                .apply();
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public int getUserId() {
        return preferences.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, null);
    }

    public String getUserEmail() {
        return preferences.getString(KEY_USER_EMAIL, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void logout() {
        preferences.edit().clear().apply();
    }
}