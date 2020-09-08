package com.example.chattestapp.Activites;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.Adapters.ChatScreenAdapter;
import com.example.chattestapp.DataBaseClasses.Chat;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatScreen extends AppCompatActivity {

    static String MESSAGE_DB = "chat";
    static String TAG = "ChatScreen";
    String recieverUid, senderUid;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    EditText et_textBody;
    Chat chat, tempChat;
    ArrayList<Chat> chats;
    ChatScreenAdapter chatScreenAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        recieverUid = getIntent().getStringExtra("uid");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        senderUid = mAuth.getUid();
        et_textBody = findViewById(R.id.chatscreen_messagebody);
        recyclerView = findViewById(R.id.chatscreen_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatScreen.this));
        chat = new Chat();
        LoadData();
    }

    private void LoadData() {

        mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    tempChat = ds.getValue(Chat.class);
                    chats.add(tempChat);
                }
                chatScreenAdapter = new ChatScreenAdapter(ChatScreen.this, chats, senderUid);
                recyclerView.setAdapter(chatScreenAdapter);
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void Send(View view) {
        String message = et_textBody.getText().toString();
        if (TextUtils.isEmpty(message)) {
            ChatUtils.maketoast(ChatScreen.this, "Please type message body");
        } else {
            chat.setSenderUid(senderUid);
            chat.setMessageBody(message);
            mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).push().setValue(chat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        mDatabase.child(MESSAGE_DB).child(recieverUid).child(senderUid).push().setValue(chat, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                Log.i(TAG, "Completed posting text");
                                ChatUtils.maketoast(ChatScreen.this, "Message Sent");
                                et_textBody.setText("");
                            }
                        });
                    }
                }
            });
        }
    }
}