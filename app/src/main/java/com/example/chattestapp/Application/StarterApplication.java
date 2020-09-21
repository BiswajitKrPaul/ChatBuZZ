package com.example.chattestapp.Application;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class StarterApplication extends Application implements LifecycleObserver {

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mDataBase = FirebaseDatabase.getInstance().getReference().child("users");
            mDataBase.child(mAuth.getCurrentUser().getUid()).child("online").setValue("true");
            mDataBase.child(mAuth.getCurrentUser().getUid()).child("online").onDisconnect().setValue("false");
            mDataBase.child(mAuth.getCurrentUser().getUid()).child("lastseen").onDisconnect().setValue(ServerValue.TIMESTAMP);
        }
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        if (mAuth.getCurrentUser() != null) {
            //Do something after 100ms
            mDataBase.child(mAuth.getCurrentUser().getUid()).child("online").setValue("false");
            mDataBase.child(mAuth.getCurrentUser().getUid()).child("lastseen").setValue(ServerValue.TIMESTAMP);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        if (mAuth.getCurrentUser() != null) {
            mDataBase.child(mAuth.getCurrentUser().getUid()).child("online").setValue("true");
        }
    }

}
