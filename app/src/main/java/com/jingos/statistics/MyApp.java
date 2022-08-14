package com.jingos.statistics;

import android.app.Application;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Constants.registerUM(this,"application");
    }
}
