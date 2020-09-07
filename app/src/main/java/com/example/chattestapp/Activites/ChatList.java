package com.example.chattestapp.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.firebase.auth.FirebaseAuth;

public class ChatList extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mAuth = FirebaseAuth.getInstance();
        ChatUtils.maketoast(ChatList.this, "Welcome back : " + mAuth.getCurrentUser().getEmail());
    }
}