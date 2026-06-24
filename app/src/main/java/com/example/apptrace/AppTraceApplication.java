package com.example.apptrace;

import android.app.Application;

public class AppTraceApplication extends Application {

    private static AppTraceApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppTraceApplication getAppContext() {
        return instance;
    }
}