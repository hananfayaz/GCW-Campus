package com.example.gcwcampus.helpers;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class FirebaseInitializationHelper extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
