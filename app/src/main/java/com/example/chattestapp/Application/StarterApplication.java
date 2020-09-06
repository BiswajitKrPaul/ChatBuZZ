package com.example.chattestapp.Application;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }
}
