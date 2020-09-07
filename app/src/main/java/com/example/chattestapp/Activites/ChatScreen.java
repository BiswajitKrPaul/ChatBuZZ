package com.example.chattestapp.Activites;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;

public class ChatScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        String uid=getIntent().getStringExtra("uid");
        ChatUtils.maketoast(ChatScreen.this, uid);
    }
}