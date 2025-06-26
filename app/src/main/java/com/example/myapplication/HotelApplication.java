package com.example.myapplication;

import android.app.Application;

public class HotelApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Room database
        AppDatabase.getDatabase(this);
    }
} 